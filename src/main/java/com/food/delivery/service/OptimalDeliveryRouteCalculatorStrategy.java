package com.food.delivery.service;

import com.food.delivery.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OptimalDeliveryRouteCalculatorStrategy implements DeliveryRouteCalculatorStrategy{

    private static final Logger logger = LoggerFactory.getLogger(OptimalDeliveryRouteCalculatorStrategy.class);

    @Override
    public Route findRoute(Map<String, Order> orderMap, Location startLocation, double speed) {
        logger.info("Calculating optimal delivery route from location={} with speed={} km/h", startLocation, speed);

        List<Task> allTasks = new ArrayList<>();

        for(Order order : orderMap.values()){
            Task pickup = new Task(order.getOrderId(), order.getPickupRestaurant().getRestId(),
                    order.getPickupRestaurant().getRestLocation(), TaskType.PICKUP);
            Task delivery = new Task(order.getOrderId(), order.getDeliverCustomer().getCustId(),
                    order.getDeliverCustomer().getCustLocation(), TaskType.DELIVERY);

            allTasks.add(pickup);
            allTasks.add(delivery);

            logger.debug("Created tasks for order {}: [PICKUP at {}, DELIVERY at {}]",
                    order.getOrderId(), pickup.getLocation(), delivery.getLocation());
        }

        List<List<Task>> validSequences = new ArrayList<>();
        generateAllSequences(allTasks,new ArrayList<>(),validSequences,new HashSet<>(),new HashSet<>());

        logger.info("Generated {} valid task sequences", validSequences.size());

        double minTime = Double.MAX_VALUE;
        List<Task> bestSequence = null;
        for(List<Task> sequence : validSequences){
            double totalTimeOfSequence = evaluateEachSequence(orderMap, sequence, startLocation, speed);
            logger.debug("Evaluated sequence with total time: {} minutes", totalTimeOfSequence);

            if(totalTimeOfSequence < minTime){
                minTime = totalTimeOfSequence;
                bestSequence = sequence;
                logger.debug("New optimal sequence found with time: {} minutes", minTime);
            }
        }

        logger.info("Optimal route selected with total time: {} minutes", Math.round(minTime));
        return new Route(bestSequence, minTime);
    }

    private double evaluateEachSequence(Map<String, Order> orderMap, List<Task> sequence, Location sourceLocation, double speed){
        double totalTime = 0;
        for(Task task : sequence){
            double distance = sourceLocation.distanceTo(task.getLocation());
            double travelTime = distance/speed * 60; //in minutes
            totalTime += travelTime;

            if(TaskType.PICKUP.equals(task.getTaskType())){
                double orderPrepTime = orderMap.get(task.getOrderId()).getPrepTimeMinutes();
                double remainingPrepTime = Math.max(orderPrepTime-totalTime,0);
                totalTime += remainingPrepTime;
            }
            sourceLocation = task.getLocation();
        }
        return totalTime;
    }


    private void generateAllSequences(List<Task> allTasks,
                                      List<Task> current,
                                      List<List<Task>> validSequences,
                                      Set<String> pickedUp,
                                      Set<String> delivered){
        if(current.size() == allTasks.size()){
            validSequences.add(new ArrayList<>(current));
            return;
        }

        for(Task task : allTasks){
            if((TaskType.PICKUP.equals(task.getTaskType()) && pickedUp.contains(task.getOrderId())) ||
                    (TaskType.DELIVERY.equals(task.getTaskType()) && delivered.contains(task.getOrderId())))
                continue;

            if(TaskType.DELIVERY.equals(task.getTaskType()) && !pickedUp.contains(task.getOrderId()))
                continue;

            current.add(task);
            if(TaskType.PICKUP.equals(task.getTaskType()))  pickedUp.add(task.getOrderId());
            else delivered.add(task.getOrderId());

            generateAllSequences(allTasks, current, validSequences, pickedUp, delivered);

            current.remove(current.size() - 1);
            if(TaskType.PICKUP.equals(task.getTaskType()))  pickedUp.remove(task.getOrderId());
            else delivered.remove(task.getOrderId());
        }
    }
}
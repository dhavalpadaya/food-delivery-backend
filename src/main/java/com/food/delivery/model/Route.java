package com.food.delivery.model;

import java.util.List;

public class Route {
    List<Task> taskList;
    double totalTimeInMinutes;

    public Route(List<Task> taskList, double totalTimeInMinutes) {
        this.taskList = taskList;
        this.totalTimeInMinutes = totalTimeInMinutes;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public double getTotalTimeInMinutes() {
        return totalTimeInMinutes;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[Route] [ " + Math.round(totalTimeInMinutes) + " minutes]");
        for(Task task : taskList){
            result.append("\n").append(task);
        }
        return result.toString();
    }
}

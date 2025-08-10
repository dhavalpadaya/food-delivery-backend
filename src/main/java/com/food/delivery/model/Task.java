package com.food.delivery.model;

public class Task {
    private String pointId; //restId or custId
    private String orderId;
    private Location location;
    private TaskType taskType;

    public Task(String orderId,
                String pointId,
                Location location,
                TaskType taskType) {
        this.orderId = orderId;
        this.pointId = pointId;
        this.location = location;
        this.taskType = taskType;
    }

    public Location getLocation() {
        return location;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        String action = this.taskType.equals(TaskType.PICKUP) ? "Pickup" : "Deliver";
        String dest = this.taskType.equals(TaskType.PICKUP) ? " from Restaurant " : " to Customer ";
        return "[Task] " + action +" Order " + this.orderId + dest + pointId +" [Location " + this.location + "]";
    }

}

package com.example.taskmanager.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticResponse {
    private long amountOfFinishedTasks;
    private long amountOfPostponedTasks;
    private long amountOfInProgressTasks;
    private long totalAmount;

    public StatisticResponse(long amountOfFinishedTasks, long amountOfPostponedTasks,
                             long amountOfInProgressTasks, long totalAmount) {
        this.amountOfFinishedTasks = amountOfFinishedTasks;
        this.amountOfPostponedTasks = amountOfPostponedTasks;
        this.amountOfInProgressTasks = amountOfInProgressTasks;
        this.totalAmount = totalAmount;
    }
}

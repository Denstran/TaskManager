package com.example.taskmanager.exceptions;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    String message;
    String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}

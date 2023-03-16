package com.example.taskmanager.exceptions;

import java.io.Serial;

public class ResourceAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}

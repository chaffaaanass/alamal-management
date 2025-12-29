package com.projects.demo.exceptions;

public class BatchTypeNotFoundException extends RuntimeException {
    public BatchTypeNotFoundException(String message) {
        super(message);
    }
}

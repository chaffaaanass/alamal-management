package com.projects.demo.exceptions;

public class EngineerNotFoundException extends RuntimeException {
    public EngineerNotFoundException(String message) {
        super(message);
    }
}

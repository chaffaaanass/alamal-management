package com.projects.demo.exceptions;

public class EngineerTypeNotFoundException extends RuntimeException {
    public EngineerTypeNotFoundException(String message) {
        super(message);
    }
}

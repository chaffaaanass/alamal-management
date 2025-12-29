package com.projects.demo.exceptions;

public class SocietyNotFoundException extends RuntimeException {
    public SocietyNotFoundException(String message) {
        super(message);
    }
}

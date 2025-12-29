package com.projects.demo.exceptions;

public class ChequeAlreadyExistsException extends RuntimeException {
    public ChequeAlreadyExistsException(String message) {
        super(message);
    }
}

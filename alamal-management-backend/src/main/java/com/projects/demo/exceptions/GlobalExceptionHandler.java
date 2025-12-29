package com.projects.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(
            GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ChequeAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleChequeAlreadyExistsException(
            ChequeAlreadyExistsException ex) {

        log.warn("Le numéro de chèque existe déjà {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Le numéro de chèque existe déjà");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EngineerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEngineerNotFoundException(
            EngineerNotFoundException ex) {

        log.warn("L'ingénieur introuvable {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "L'ingénieur introuvable");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EngineerTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEngineerTypeNotFoundException(
            EngineerTypeNotFoundException ex) {

        log.warn("Le type de l'ingénieur introuvable {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Le type de l'ingénieur introuvable");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException ex) {

        log.warn("L'utilisateur introuvable {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "L'utilisateur introuvable");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BatchTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBatchTypeNotFoundException(
            BatchTypeNotFoundException ex) {

        log.warn("Le lot introuvable {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Le lot introuvable");
        return ResponseEntity.badRequest().body(errors);
    }
}

package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Log exception Message
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        // Log exception Message
        return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }
}

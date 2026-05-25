package com.ticketing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: implemented in Phase X
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}

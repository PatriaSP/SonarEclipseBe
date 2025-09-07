package com.patria.apps.controller.advice;

import com.patria.apps.exception.ExceptionResponse;
import com.patria.apps.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ExceptionResponse> generalException(GeneralException ex) {
        ex.printStackTrace();
        ExceptionResponse errorResponse = new ExceptionResponse(ex.getStatus(), ex.getMessage(), ex.getStatusCode());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NoHandlerFoundException ex) {
        ex.printStackTrace();
        ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Not Found!", HttpStatus.NOT_FOUND);
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exception(Exception ex) {
        System.err.println(ex.getMessage());
        ex.printStackTrace();
        ExceptionResponse errorResponse = new ExceptionResponse(500, "Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(500)
                .body(errorResponse);
    }
}

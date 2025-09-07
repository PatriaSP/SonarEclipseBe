package com.patria.apps.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {

    private int status;
    private String message;
    private HttpStatus statusCode;

    public GeneralException(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.statusCode = status;
    }

    public GeneralException(HttpStatus status, String message, Throwable cause) {
        super(cause); 
        this.status = status.value();
        this.message = message;
        this.statusCode = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

}

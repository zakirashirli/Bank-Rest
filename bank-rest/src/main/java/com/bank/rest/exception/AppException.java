package com.bank.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom runtime exception that allows attaching an HTTP status code.
 * Example usage:
 *   throw new AppException(HttpStatus.BAD_REQUEST, "Insufficient balance");
 */
public class AppException extends RuntimeException {

    private final HttpStatus status;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}


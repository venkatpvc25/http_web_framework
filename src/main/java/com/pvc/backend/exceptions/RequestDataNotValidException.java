package com.pvc.backend.exceptions;

public class RequestDataNotValidException extends RuntimeException {
    public RequestDataNotValidException(String message) {
        super(message);
    }
}

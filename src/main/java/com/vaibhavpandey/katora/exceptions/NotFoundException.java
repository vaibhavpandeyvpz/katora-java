package com.vaibhavpandey.katora.exceptions;

public class NotFoundException extends IllegalArgumentException {

    public NotFoundException(String message) {
        super(message);
    }
}

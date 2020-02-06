package com.vaibhavpandey.katora.exceptions;

public class FactoryException extends RuntimeException {

    public FactoryException(String message, Throwable e) {
        super(message, e);
    }
}

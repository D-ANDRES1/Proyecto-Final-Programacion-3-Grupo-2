package com.grupo2.treeengine.exception;

public class InvalidNodeValueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidNodeValueException(String value) {
        super("Invalid node value: " + value);
    }
}
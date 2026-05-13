package com.grupo2.treeengine.exception;

public class RootAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RootAlreadyExistsException() {
        super("Root already exists");
    }
}
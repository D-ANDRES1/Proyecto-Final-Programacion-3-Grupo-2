package com.grupo2.treeengine.exception;

public class CycleDetectedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CycleDetectedException(String string) {
        super("Cycle detected in tree structure");
    }
}
package com.grupo2.treeengine.exception;

public class NodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NodeNotFoundException(String nodeId) {
        super("Node not found: " + nodeId);
    }
}
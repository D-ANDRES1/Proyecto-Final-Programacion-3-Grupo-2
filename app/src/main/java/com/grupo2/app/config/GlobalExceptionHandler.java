package com.grupo2.app.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grupo2.treeengine.exception.CycleDetectedException;
import com.grupo2.treeengine.exception.InvalidNodeValueException;
import com.grupo2.treeengine.exception.InvalidOperationException;
import com.grupo2.treeengine.exception.NodeNotFoundException;
import com.grupo2.treeengine.exception.RootAlreadyExistsException;
import com.grupo2.treeengine.exception.TreeException;

/**
 * Captura las excepciones del tree-engine y las convierte en respuestas HTTP estándar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // MÉTODO AUXILIAR PARA ERRORES
    // =========================
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }

    // =========================
    // MANEJO DE EXCEPCIONES
    // =========================

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNodeNotFound(NodeNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RootAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRootExists(RootAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidNodeValueException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidValue(InvalidNodeValueException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CycleDetectedException.class)
    public ResponseEntity<Map<String, Object>> handleCycle(CycleDetectedException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOp(InvalidOperationException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TreeException.class)
    public ResponseEntity<Map<String, Object>> handleTreeError(TreeException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }
}
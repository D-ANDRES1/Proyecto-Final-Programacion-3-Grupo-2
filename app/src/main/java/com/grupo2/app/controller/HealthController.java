package com.grupo2.app.controller;

import com.grupo2.app.api.HealthApi;
import com.grupo2.app.model.HealthResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthApi {

	@Override
    public ResponseEntity<HealthResponse> healthCheck() {

        HealthResponse health = new HealthResponse();
        health.setStatus("UP");

        return ResponseEntity.ok(health);
    }
}

package com.example.demo.controller;

import com.example.demo.model.LogEvent;
import com.example.demo.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<Map<String, String>> ingestLog(@RequestBody LogEvent logEvent) {
        try {
            ingestionService.processLog(logEvent);
            return ResponseEntity.ok(Map.of("status", "accepted", "message", "Log ingested"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body((Map.of("status", "server error", "message", e.getMessage())));
        }
    }

}
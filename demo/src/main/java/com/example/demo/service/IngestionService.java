package com.example.demo.service;

import com.example.demo.model.LogEvent;
import com.example.demo.repository.LogEventRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class IngestionService {

    private final LogEventRepository repository;

    // Constructor
    public IngestionService(LogEventRepository repository) {
        this.repository = repository;
    }
    // event check
    public void processLog(LogEvent logEvent) {
        if (logEvent.getEventType() == null || logEvent.getEventType().isEmpty()) {
            throw new IllegalArgumentException("Event type is required");
        }

        // timestamp check
        if (logEvent.getTimestamp() == null) {
            logEvent.setTimestamp(Instant.now());
        }

        // dup check
        Instant fiveMinutesAgo = logEvent.getTimestamp().minus(5, ChronoUnit.MINUTES);

        boolean exists = repository.existsByEventTypeAndSourceIpAndUserIdAndTimestampAfter(
                logEvent.getEventType(),
                logEvent.getSourceIp(),
                logEvent.getUserId(),
                fiveMinutesAgo
        );
        if (exists) {
            throw new IllegalArgumentException("Duplicate log detected");
        }

        repository.save(logEvent);
    }

}

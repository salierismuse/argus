package com.example.demo.repository;

import com.example.demo.model.LogEvent;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.time.Instant;

@Repository

public interface LogEventRepository extends JpaRepository<LogEvent, Long> {
    boolean existsByEventTypeAndSourceIpAndUserIdAndTimestampAfter(
            String eventType,
            String sourceIp,
            String userId,
            Instant timestamp
    );
}

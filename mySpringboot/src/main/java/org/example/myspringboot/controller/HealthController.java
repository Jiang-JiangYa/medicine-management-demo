package org.example.myspringboot.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
@RestController
public class HealthController {
    private final JdbcTemplate jdbc;
    public HealthController(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @GetMapping("/health") public ResponseEntity<?> health() {
        try { jdbc.queryForObject("SELECT 1", Integer.class); return ResponseEntity.ok(Collections.singletonMap("status", "up")); }
        catch (Exception e) { return ResponseEntity.status(503).body(Collections.singletonMap("status", "unavailable")); }
    }
}

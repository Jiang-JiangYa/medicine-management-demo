package org.example.myspringboot;
import org.junit.jupiter.api.Test;
import org.example.myspringboot.controller.HealthController;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class HealthTests {
 @Test void healthyDatabaseReturns200() {
  JdbcTemplate jdbc=mock(JdbcTemplate.class);when(jdbc.queryForObject("SELECT 1",Integer.class)).thenReturn(1);
  assertEquals(200,new HealthController(jdbc).health().getStatusCodeValue());
 }
 @Test void unavailableDatabaseReturns503WithoutDetails() {
  JdbcTemplate jdbc=mock(JdbcTemplate.class);when(jdbc.queryForObject("SELECT 1",Integer.class)).thenThrow(new RuntimeException("private connection details"));
  org.springframework.http.ResponseEntity<?> result=new HealthController(jdbc).health();
  assertEquals(503,result.getStatusCodeValue());assertFalse(result.getBody().toString().contains("private"));
 }
}

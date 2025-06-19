package br.com.pizzutti.chatws.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LocalDateTime now() {
        String sql = "SELECT NOW()";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            var timestamp = rs.getTimestamp(1);
            return timestamp.toLocalDateTime();
        });
    }
}

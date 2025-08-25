package br.com.pizzutti.chatinho.api.infra.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
public class TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LocalDateTime now() {
        String sql = "SELECT NOW()";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            var instant = rs.getTimestamp(1).toInstant();
            return LocalDateTime.ofInstant(instant, ZoneId.of("America/Sao_Paulo"));
        });
    }
}

package br.com.pizzutti.chatinho.api.infra.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TimeService {

    private static Duration offset;

    public TimeService(TimeRepository timeRepository) {
        LocalDateTime dbNow = timeRepository.now();
        LocalDateTime localNow = LocalDateTime.now();
        offset = Duration.between(localNow, dbNow);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now().plus(offset);
    }
}

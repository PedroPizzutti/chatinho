package br.com.pizzutti.chatinho.api.infra.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeService {
    @Getter
    private static TimeService instance = null;
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
        instance = this;
    }

    public LocalDateTime now() {
        return this.timeRepository.now();
    }
}

package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.repository.TimeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public LocalDateTime now() {
        return this.timeRepository.now();
    }

}

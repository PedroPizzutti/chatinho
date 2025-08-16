package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.repository.TimeRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeComponent {
    @Getter
    private static TimeComponent instance = null;
    private final TimeRepository timeRepository;

    public TimeComponent(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
        instance = this;
    }

    public LocalDateTime now() {
        return this.timeRepository.now();
    }

}

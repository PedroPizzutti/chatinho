package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.model.Totem;
import br.com.pizzutti.chatws.repository.TotemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class TotemService {

    private final TotemRepository totemRepository;

    public TotemService(TotemRepository totemRepository) {
        this.totemRepository = totemRepository;
    }

    private Totem findByGuid(String guid) {
        return this.totemRepository
                .findByGuid(guid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Totem inválido!"));
    }

    private void invalidate(Totem totem) {
        totem.setUsed(true);
        this.totemRepository.save(totem);
    }

    private void validateIsUsed(Totem totem) {
        if (totem.getUsed()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Totem já utilizado!");
        }
    }

    private void validateIsExpired(Totem totem) {
        if (LocalDateTime.now().isAfter((totem.getCreatedAt().plusHours(totem.getExpiresIn())))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Totem expirado!");
        }
    }

    public void burn(String guid) {
        var totem = this.findByGuid(guid);
        this.validateIsUsed(totem);
        this.validateIsExpired(totem);
        this.invalidate(totem);
    }
}

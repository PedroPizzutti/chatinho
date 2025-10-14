package br.com.pizzutti.chatinho.api.domain.totem;

import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TotemService extends FilterService<Totem> {

    private final TotemRepository totemRepository;

    public TotemService(TotemRepository totemRepository) {
        super();
        this.totemRepository = totemRepository;
    }

    private Totem findByGuid(String guid) {
        try {
            return this.totemRepository
                    .findOne(super.filter("guid", guid, FilterOperationEnum.EQUAL).specification())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Totem inválido!"));
        } finally {
            super.reset();
        }
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
        if (TimeService.now().isAfter((totem.getCreatedAt().plusHours(totem.getExpiresIn())))) {
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

package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.model.Totem;
import br.com.pizzutti.chatws.repository.TotemRepository;
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
        var spec = super.reset().filter("guid", guid, FilterOperationEnum.EQUAL).specification();
        return this.totemRepository
                .findOne(spec)
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
        if (TimeComponent.getInstance().now().isAfter((totem.getCreatedAt().plusHours(totem.getExpiresIn())))) {
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

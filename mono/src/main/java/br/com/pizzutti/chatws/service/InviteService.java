package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.dto.InviteInputDto;
import br.com.pizzutti.chatws.enums.FilterDirectionEnum;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.enums.InviteStatusEnum;
import br.com.pizzutti.chatws.model.Invite;
import br.com.pizzutti.chatws.repository.InviteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InviteService extends FilterService<Invite> {

    private final InviteRepository inviteRepository;

    public InviteService(InviteRepository inviteRepository) {
        super();
        this.inviteRepository = inviteRepository;
    }

    public <U> InviteService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    public InviteService orderBy(String property, FilterDirectionEnum direction) {
        super.orderBy(property, direction);
        return this;
    }

    public List<Invite> find() {
        try {
            return this.inviteRepository.findAll(super.specification(), super.sort());
        } finally {
            super.reset();
        }
    }

    public Invite findById(Long id) {
        return this.inviteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Convite não encontrado!"));
    }

    public Invite create(InviteInputDto inviteInputDto) {
        var invite = Invite.builder()
                .createdAt(TimeComponent.getInstance().now())
                .status(InviteStatusEnum.PENDING)
                .idUserFrom(inviteInputDto.idUserFrom())
                .idUserTo(inviteInputDto.idUserTo())
                .idRoom(inviteInputDto.idRoom())
                .build();
        return this.inviteRepository.saveAndFlush(invite);
    }

    public Invite update(Invite invite) {
        return this.inviteRepository.saveAndFlush(invite);
    }
}

package br.com.pizzutti.chatinho.api.domain.room;

import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoomService extends FilterService<Room> {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        super();
        this.roomRepository = roomRepository;
    }

    public <U> RoomService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    public Room create(RoomInputDto roomInputDto, Long owner) {
        var room = Room.builder()
                .idOwner(owner)
                .name(roomInputDto.name())
                .createdAt(TimeService.now())
                .build();

        this.roomRepository.saveAndFlush(room);
        return room;
    }

    public void delete(Long id) {
        this.roomRepository.deleteById(id);
    }

    public List<Room> find() {
        try {
            return this.roomRepository.findAll(super.specification());
        } finally {
            super.reset();
        }
    }

    public Room findById(Long id) {
        return this.roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada!"));
    }

}

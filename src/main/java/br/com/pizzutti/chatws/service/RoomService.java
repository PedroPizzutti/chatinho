package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.dto.RoomInsertDto;
import br.com.pizzutti.chatws.model.Room;
import br.com.pizzutti.chatws.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room create(RoomInsertDto roomInsertDto, Long owner) {
        var room = Room.builder()
                .owner(owner)
                .name(roomInsertDto.name())
                .createdAt(TimeComponent.getInstance().now())
                .build();

        this.roomRepository.saveAndFlush(room);
        return room;
    }

    public List<Room> findAll() {
        return this.roomRepository.findAll();
    }

    public Room findById(Long id) {
        return this.roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada!"));
    }

}

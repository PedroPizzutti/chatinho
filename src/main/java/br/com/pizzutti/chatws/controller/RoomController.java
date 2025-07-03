package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.RoomDto;
import br.com.pizzutti.chatws.dto.RoomInsertDto;
import br.com.pizzutti.chatws.dto.RoomAggregateDto;
import br.com.pizzutti.chatws.facade.RoomFacade;
import br.com.pizzutti.chatws.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/room")
public class RoomController {

    private final RoomFacade roomFacade;
    private final RoomService roomService;

    public RoomController(RoomFacade roomFacade,
                          RoomService roomService) {
        this.roomFacade = roomFacade;
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomAggregateDto> create(@RequestBody @Valid RoomInsertDto roomInsertDto) {
        var user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var room = this.roomFacade.create(roomInsertDto, Long.parseLong(user));
        return ResponseEntity.status(201).body(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> list() {
        var rooms = this.roomService.findAll().stream().map(RoomDto::fromRoom).toList();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomAggregateDto> findById(@PathVariable Long id) {
        var room = this.roomFacade.findById(id);
        return ResponseEntity.ok(room);
    }




}

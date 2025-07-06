package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.AdviceDto;
import br.com.pizzutti.chatws.dto.RoomDto;
import br.com.pizzutti.chatws.dto.RoomInsertDto;
import br.com.pizzutti.chatws.dto.RoomAggregateDto;
import br.com.pizzutti.chatws.facade.RoomFacade;
import br.com.pizzutti.chatws.service.RoomService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Salas")
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = RoomAggregateDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<RoomAggregateDto> create(@RequestBody @Valid RoomInsertDto roomInsertDto) {
        var user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var room = this.roomFacade.create(roomInsertDto, Long.parseLong(user));
        return ResponseEntity.status(201).body(room);
    }

    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomAggregateDto.class)))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<List<RoomDto>> list() {
        var rooms = this.roomService.findAll().stream().map(RoomDto::fromRoom).toList();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RoomAggregateDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<RoomAggregateDto> findById(@PathVariable Long id) {
        var room = this.roomFacade.findById(id);
        return ResponseEntity.ok(room);
    }




}

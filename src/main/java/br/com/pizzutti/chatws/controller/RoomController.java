package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.*;
import br.com.pizzutti.chatws.facade.RoomFacade;
import br.com.pizzutti.chatws.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
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

    public RoomController(RoomFacade roomFacade) {
        this.roomFacade = roomFacade;
    }

    @PostMapping
    @Operation(summary = "Cria uma sala")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = RoomAggregateDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<RoomAggregateDto> create(@RequestBody @Valid RoomInsertDto roomInsertDto) {
        var room = this.roomFacade.create(roomInsertDto, this.getIdUserLogged());
        return ResponseEntity.status(201).body(room);
    }

    @GetMapping
    @Operation(summary = "Lista as salas do usuário logado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomAggregateDto.class)))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<List<RoomDto>> list() {
        var rooms = this.roomFacade.findAllByUser(this.getIdUserLogged());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém os dados detalhados de uma sala")
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

    @GetMapping("/{id}/message")
    @Operation(summary = "Obtém as mensagens de uma sala (listadas e ordenadas pelas últimas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MessagePageDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<MessagePageDto> listMessages(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer perPage,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(this.roomFacade.findMessages(id, page, perPage));
    }

    private Long getIdUserLogged() {
        return Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}

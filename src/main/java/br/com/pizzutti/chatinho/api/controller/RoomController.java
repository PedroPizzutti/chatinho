package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.domain.message.MessageGetAggregatePageDto;
import br.com.pizzutti.chatinho.api.domain.room.RoomGetAggregateDto;
import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.room.RoomPostDto;
import br.com.pizzutti.chatinho.api.domain.room.RoomFacade;
import br.com.pizzutti.chatinho.api.domain.user.User;
import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jose4j.jwk.Use;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        @ApiResponse(
                responseCode = "201",
                description = "CREATED",
                content = @Content(schema = @Schema(implementation = RoomGetAggregateDto.class)))
    })
    public ResponseEntity<RoomGetAggregateDto> create(@RequestBody @Valid RoomPostDto roomPostDto,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(this.roomFacade.create(roomPostDto, user.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma sala")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        this.roomFacade.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    };

    @PatchMapping("/{id}/leave")
    @Operation(summary = "Sai de uma sala")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    })
    public ResponseEntity<Void> leave(@PathVariable Long id,
                                      @AuthenticationPrincipal User user) {
        this.roomFacade.leave(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/kick/{idMember}")
    public ResponseEntity<Void> kick(@PathVariable Long id,
                                     @PathVariable Long idMember,
                                     @AuthenticationPrincipal User user) {
        this.roomFacade.kick(id, idMember, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Lista as salas do usuário logado")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomGetAggregateDto.class)))),
    })
    public ResponseEntity<List<RoomGetDto>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.roomFacade.findAllByUser(user.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém os dados detalhados de uma sala")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = RoomGetAggregateDto.class)))
    })
    public ResponseEntity<RoomGetAggregateDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.roomFacade.findById(id));
    }

    @GetMapping("/{id}/message")
    @Operation(summary = "Obtém as mensagens de uma sala (listadas e ordenadas pelas últimas)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MessageGetAggregatePageDto.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<MessageGetAggregatePageDto> listMessages(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer perPage,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(this.roomFacade.findMessages(id, page, perPage));
    }
}

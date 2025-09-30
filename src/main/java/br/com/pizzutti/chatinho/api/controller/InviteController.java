package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import br.com.pizzutti.chatinho.api.domain.invite.InviteAggregateDto;
import br.com.pizzutti.chatinho.api.domain.invite.InviteInputDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.domain.invite.InviteStatusEnum;
import br.com.pizzutti.chatinho.api.domain.invite.InviteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "Convites")
@RestController
@RequestMapping("v1/invite")
public class InviteController {

    private final InviteFacade inviteFacade;

    public InviteController(InviteFacade inviteFacade) {
        this.inviteFacade = inviteFacade;
    }

    @PostMapping
    @Operation(summary = "Cria um convite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = InviteAggregateDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<InviteAggregateDto> create(@RequestBody @Valid InviteInputDto inviteInputDto) {
        return ResponseEntity.status(201).body(this.inviteFacade.create(inviteInputDto));
    }

    @PatchMapping("{id}/accept")
    @Operation(summary = "Aceita um convite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", content = @Content(schema = @Schema(implementation = InviteAggregateDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<InviteAggregateDto> accept(@PathVariable Long id) {
        return ResponseEntity.status(202).body(this.inviteFacade.accept(id));
    }

    @PatchMapping("{id}/reject")
    @Operation(summary = "Rejeita um convite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", content = @Content(schema = @Schema(implementation = InviteAggregateDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<InviteAggregateDto> reject(@PathVariable Long id) {
        return ResponseEntity.status(202).body(this.inviteFacade.reject(id));
    }

    @GetMapping
    @Operation(summary = "Lista os convites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InviteAggregateDto.class)))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<List<InviteAggregateDto>> list(
            @RequestParam(required = false) Long idUserFrom,
            @RequestParam(required = false) Long idUserTo,
            @RequestParam(required = false) Long idRoom,
            @RequestParam(required = false) InviteStatusEnum status
    ) {
        return ResponseEntity.ok(this.inviteFacade.listInvite(idUserFrom, idUserTo, idRoom, status));
    }
}

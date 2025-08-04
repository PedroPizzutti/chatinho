package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.AdviceDto;
import br.com.pizzutti.chatws.dto.InviteAggregateDto;
import br.com.pizzutti.chatws.dto.InviteInputDto;
import br.com.pizzutti.chatws.dto.RoomAggregateDto;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.enums.InviteStatusEnum;
import br.com.pizzutti.chatws.facade.InviteFacade;
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
        var invite = inviteFacade.create(inviteInputDto);
        return ResponseEntity.status(201).body(invite);
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
        var invite = inviteFacade.accept(id);
        return ResponseEntity.status(202).body(invite);
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
        var invite = inviteFacade.reject(id);
        return ResponseEntity.status(202).body(invite);
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
        var listInvite = this.inviteFacade
                .filter("status", Objects.isNull(status) ? null : status.toString(), FilterOperationEnum.EQUAL)
                .filter("idUserFrom", idUserFrom, FilterOperationEnum.EQUAL)
                .filter("idUserTo", idUserTo, FilterOperationEnum.EQUAL)
                .filter("idRoom", idRoom, FilterOperationEnum.EQUAL)
                .list();
        return ResponseEntity.ok(listInvite);
    }
}

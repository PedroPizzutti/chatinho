package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.domain.invite.*;
import br.com.pizzutti.chatinho.api.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED")})
    public ResponseEntity<InviteGetAggregateDto> create(@RequestBody @Valid InvitePostDto invitePostDto,
                                                        @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(this.inviteFacade.create(invitePostDto, user.getId()));
    }

    @GetMapping("/receive")
    @Operation(summary = "Lista os convites recebidos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),})
    public ResponseEntity<List<InviteGetAggregateDto>> receive(@RequestParam(required = false) InviteStatusEnum status,
                                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.inviteFacade.get(new InviteFilterDto(user.getId(), 0L, status)));
    }

    @GetMapping("/send")
    @Operation(summary = "Lista os convites enviados")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<InviteGetAggregateDto>> send(@RequestParam(required = false) InviteStatusEnum status,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.inviteFacade.get(new InviteFilterDto(0L, user.getId(), status)));
    }

    @PatchMapping("{id}/accept")
    @Operation(summary = "Aceita um convite")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO_CONTENT")})
    public ResponseEntity<Void> accept(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        this.inviteFacade.patchStatus(id, user.getId(), InviteStatusEnum.ACCEPTED);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/reject")
    @Operation(summary = "Rejeita um convite")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO_CONTENT")})
    public ResponseEntity<InviteGetAggregateDto> reject(@PathVariable Long id,
                                                        @AuthenticationPrincipal User user) {
        this.inviteFacade.patchStatus(id, user.getId(), InviteStatusEnum.REJECTED);
        return ResponseEntity.noContent().build();
    }
}

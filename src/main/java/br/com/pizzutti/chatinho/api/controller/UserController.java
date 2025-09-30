package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import br.com.pizzutti.chatinho.api.domain.user.UserInputDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.domain.user.UserFacade;
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

@Tag(name = "Usuário")
@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping("new")
    @Operation(summary = "Cria um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserInputDto userInputDto) {
        return ResponseEntity.status(201).body(this.userFacade.createUser(userInputDto));
    }

    @GetMapping
    @Operation(summary = "Lista os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<List<UserDto>> listUsers(
            @RequestParam(value = "nick", required = false) String nick,
            @RequestParam(value = "login", required = false) String login
    ) {
        return ResponseEntity.ok(this.userFacade.listUser(nick, login));
    }
}

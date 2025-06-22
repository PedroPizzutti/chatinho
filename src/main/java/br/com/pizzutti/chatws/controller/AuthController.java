package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.*;
import br.com.pizzutti.chatws.facade.UserFacade;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final UserFacade userFacade;

    public AuthController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserLoggedDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<UserLoggedDto> login(@RequestBody UserLoginDto userLoginDto) {
        var userLoggedDto = this.userFacade.loginApi(userLoginDto);
        return ResponseEntity.status(201).body(userLoggedDto);
    }

    @PostMapping("/create-user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserCreatedDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<UserCreatedDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        var user = this.userFacade.createUser(userCreateDto);
        return ResponseEntity.status(201).body(user);
    }
}

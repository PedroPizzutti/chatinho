package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.domain.auth.LoginDto;
import br.com.pizzutti.chatinho.api.domain.auth.RefreshTokenDto;
import br.com.pizzutti.chatinho.api.domain.token.TokenDto;
import br.com.pizzutti.chatinho.api.domain.user.UserTokenDto;
import br.com.pizzutti.chatinho.api.domain.auth.AuthFacade;
import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping("/login")
    @Operation(summary = "Loga um usuário no sistema, obtendo um token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserTokenDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<UserTokenDto> login(@RequestBody LoginDto loginDto) {
        var userTokenDto = this.authFacade.loginApi(loginDto);
        return ResponseEntity.status(201).body(userTokenDto);
    }

    @PostMapping("/refresh-login")
    @Operation(summary = "Renova o token de um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserTokenDto.class))),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = AdviceDto.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = AdviceDto.class)))
    })
    public ResponseEntity<TokenDto> refreshLogin(@RequestBody RefreshTokenDto refreshTokenDto) {
        var tokenDto = this.authFacade.refreshLoginApi(refreshTokenDto);
        return ResponseEntity.status(201).body(tokenDto);
    }
}

package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.domain.auth.LoginPostDto;
import br.com.pizzutti.chatinho.api.domain.auth.RefreshTokenPostDto;
import br.com.pizzutti.chatinho.api.domain.token.TokenGetDto;
import br.com.pizzutti.chatinho.api.domain.token.TokenGetAggregateDto;
import br.com.pizzutti.chatinho.api.domain.auth.AuthFacade;
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
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED")})
    public ResponseEntity<TokenGetAggregateDto> login(@RequestBody LoginPostDto loginPostDto) {
        return ResponseEntity.status(201).body(this.authFacade.loginApi(loginPostDto));
    }

    @PostMapping("/refresh-login")
    @Operation(summary = "Renova o token de um usuário")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED")})
    public ResponseEntity<TokenGetDto> refreshLogin(@RequestBody RefreshTokenPostDto refreshTokenPostDto) {
        return ResponseEntity.status(201).body(this.authFacade.refreshLoginApi(refreshTokenPostDto));
    }
}

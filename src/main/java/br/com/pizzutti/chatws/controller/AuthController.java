package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserCreatedDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.facade.UserFacade;
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
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        var token = this.userFacade.generateToken(userLoginDto);
        return ResponseEntity.status(201).body(token);
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserCreatedDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        var user = this.userFacade.createUser(userCreateDto);
        return ResponseEntity.status(201).body(user);
    }
}

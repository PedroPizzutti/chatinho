package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.UserServiceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final UserServiceFacade userServiceFacade;

    public AuthController(UserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        var token = this.userServiceFacade.login(userLoginDto);
        return ResponseEntity.status(201).body(token);
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto userCreateDto) {
        var user = this.userServiceFacade.createdUser(userCreateDto);
        return ResponseEntity.status(201).body(user);
    }
}

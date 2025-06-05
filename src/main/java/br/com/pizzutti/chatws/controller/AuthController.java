package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.UserService;
import br.com.pizzutti.chatws.service.UserTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserTokenService userTokenService;

    public AuthController(UserService userService, UserTokenService userTokenService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        this.userTokenService.useToken(createUserDto);
        var user = this.userService.create(createUserDto);
        return ResponseEntity.status(201).body(user);
    }

}

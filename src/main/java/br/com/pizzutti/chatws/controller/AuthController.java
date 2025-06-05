package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.service.UserTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final UserTokenService userTokenService;

    public AuthController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto) {
        var token = userTokenService.findByToken(createUserDto.token());
        userTokenService.validateToken(token);
        userTokenService.burnToken(token);
        return ResponseEntity.status(201).body(null);
    }

}

package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.UserService;
import br.com.pizzutti.chatws.service.TotemService;
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

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var user = this.userServiceFacade.createdUser(createUserDto);
        return ResponseEntity.status(201).body(user);
    }

}

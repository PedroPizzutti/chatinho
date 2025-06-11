package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceFacade {

    private final UserService userService;
    private final TotemService totemService;
    private final TokenService tokenService;

    public UserServiceFacade(UserService userService, TotemService totemService, TokenService tokenService) {
        this.userService  = userService;
        this.totemService = totemService;
        this.tokenService = tokenService;
    }

    public User login(String token) {
        var login = this.tokenService.validateToken(token);
        return this.userService.findByLogin(login);
    }

    public User createdUser(UserCreateDto userCreateDto) {
        this.totemService.burn(userCreateDto.totem());
        return this.userService.create(userCreateDto);
    }

    public TokenDto generateToken(UserLoginDto userLoginDto) {
        var user = this.userService.login(userLoginDto);
        return this.tokenService.generateToken(user);
    }

}

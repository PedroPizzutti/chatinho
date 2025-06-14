package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.TokenService;
import br.com.pizzutti.chatws.service.TotemService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;
    private final TotemService totemService;
    private final TokenService tokenService;

    public UserFacade(UserService userService, TotemService totemService, TokenService tokenService) {
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

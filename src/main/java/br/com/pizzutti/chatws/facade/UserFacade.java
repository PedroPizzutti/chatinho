package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.*;
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

    public UserLoggedDto loginApi(UserLoginDto userLoginDto) {
        var userCreatedDto = this.userService.login(userLoginDto);
        var tokenDto = this.tokenService.generateToken(userCreatedDto.login());
        return UserLoggedDto.builder()
                .user(userCreatedDto)
                .token(tokenDto)
                .build();
    }

    public User loginWebSocket(String token) {
        var login = this.tokenService.validateAccessToken(token);
        return this.userService.findByLogin(login);
    }

    public UserCreatedDto createUser(UserCreateDto userCreateDto) {
        this.totemService.burn(userCreateDto.totem());
        return this.userService.create(userCreateDto);
    }
}

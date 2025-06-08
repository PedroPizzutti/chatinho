package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceFacade {

    private final UserService userService;
    private final TotemService totemService;

    public UserServiceFacade(UserService userService, TotemService totemService) {
        this.userService  = userService;
        this.totemService = totemService;
    }

    public User createdUser(UserCreateDto userCreateDto) {
        this.totemService.burn(userCreateDto.totem());
        return this.userService.create(userCreateDto.login(), userCreateDto.password());
    }

    public User login(UserLoginDto userLoginDto) {
        return this.userService.login(userLoginDto);
    }

}

package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.CreateUserDto;
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

    public User createdUser(CreateUserDto createUserDto) {
        this.totemService.burn(createUserDto.totem());
        return this.userService.create(createUserDto.login(), createUserDto.password());
    }

}

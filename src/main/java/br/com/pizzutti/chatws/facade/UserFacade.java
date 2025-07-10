package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.UserDto;
import br.com.pizzutti.chatws.dto.UserInputDto;
import br.com.pizzutti.chatws.service.TotemService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;
    private final TotemService totemService;

    public UserFacade(UserService userService,
                      TotemService totemService) {
        this.userService = userService;
        this.totemService = totemService;
    }

    public UserDto createUser(UserInputDto userInputDto) {
        this.totemService.burn(userInputDto.totem());
        return this.userService.create(userInputDto);
    }

}

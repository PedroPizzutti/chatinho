package br.com.pizzutti.chatinho.api.domain.user;

import br.com.pizzutti.chatinho.api.domain.totem.TotemService;
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

    public UserService userService() {
        return this.userService;
    }

}

package br.com.pizzutti.chatinho.api.domain.user;

import br.com.pizzutti.chatinho.api.domain.totem.TotemService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFacade {

    private final UserService userService;
    private final TotemService totemService;

    public UserFacade(UserService userService,
                      TotemService totemService) {
        this.userService = userService;
        this.totemService = totemService;
    }

    @Transactional
    public UserDto createUser(UserInputDto userInputDto) {
        this.totemService.burn(userInputDto.totem());
        var user = this.userService.create(userInputDto);
        return UserDto.fromUser(user);
    }

    public List<UserDto> listUser(String nickname, String login) {
        return this.userService
            .filter("nickname", nickname, FilterOperationEnum.LIKE)
            .filter("login", login, FilterOperationEnum.LIKE)
            .find()
            .stream()
            .map(UserDto::fromUser)
            .toList();
    }
}

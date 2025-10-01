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
    public UserGetDto createUser(UserPostDto userPostDto) {
        this.totemService.burn(userPostDto.totem());
        var user = this.userService.create(userPostDto);
        return UserGetDto.fromUser(user);
    }

    public List<UserGetDto> listUser(String nickname, String login) {
        return this.userService
            .filter("nickname", nickname, FilterOperationEnum.LIKE)
            .filter("login", login, FilterOperationEnum.LIKE)
            .find()
            .stream()
            .map(UserGetDto::fromUser)
            .toList();
    }
}

package br.com.pizzutti.chatinho.api.domain.auth;

import br.com.pizzutti.chatinho.api.domain.token.TokenDto;
import br.com.pizzutti.chatinho.api.domain.user.User;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import br.com.pizzutti.chatinho.api.domain.user.UserService;
import br.com.pizzutti.chatinho.api.domain.user.UserTokenDto;
import br.com.pizzutti.chatinho.api.domain.token.TokenService;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthFacade(UserService userService, TokenService tokenService) {
        this.userService  = userService;
        this.tokenService = tokenService;
    }

    public UserTokenDto loginApi(LoginDto loginDto) {
        var userCreatedDto = this.userService.login(loginDto);
        var tokenDto = this.tokenService.generateToken(userCreatedDto);
        return UserTokenDto.builder()
                .user(userCreatedDto)
                .token(tokenDto)
                .build();
    }

    public TokenDto refreshLoginApi(RefreshTokenDto refreshTokenDto) {
        var userId = this.tokenService.validateRefreshToken(refreshTokenDto.jwt());
        var user = this.userService.findById(Long.parseLong(userId));
        return this.tokenService.generateToken(UserDto.fromUser(user));
    }

    public User loginWebSocket(String token) {
        var userId = this.tokenService.validateAccessToken(token);
        return this.userService.findById(Long.parseLong(userId));
    }
}

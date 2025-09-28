package br.com.pizzutti.chatinho.api.domain.auth;

import br.com.pizzutti.chatinho.api.domain.token.TokenDto;
import br.com.pizzutti.chatinho.api.domain.user.User;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import br.com.pizzutti.chatinho.api.domain.user.UserService;
import br.com.pizzutti.chatinho.api.domain.user.UserTokenDto;
import br.com.pizzutti.chatinho.api.domain.token.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthFacade(UserService userService,
                      TokenService tokenService,
                      AuthenticationManager authenticationManager) {
        this.userService  = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    public UserTokenDto loginApi(LoginDto loginDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.login(), loginDto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var userCreatedDto = UserDto.fromUser((User) authentication.getPrincipal());
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
        try {
            var userId = this.tokenService.validateAccessToken(token);
            return this.userService.findById(Long.parseLong(userId));
        } catch (Exception e) {
            throw new SecurityException("token inválido!", e);
        }
    }
}

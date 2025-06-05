package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.model.UserToken;
import br.com.pizzutti.chatws.repository.UserTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    public UserTokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    private UserToken findByToken(String token) {
        return this.userTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token não encontrado!"));
    }

    private void burnToken(UserToken userToken) {
        userToken.setUsed(true);
        this.userTokenRepository.save(userToken);
    }

    private void validateTokenIsUsed(UserToken userToken) {
        if (userToken.getUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token já utilizado!");
        }
    }

    private void validateTokenIsExpired(UserToken userToken) {
        if (LocalDateTime.now().isAfter((userToken.getCreatedAt().plusHours(userToken.getExpiresIn())))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado!");
        }
    }

    public void useToken(CreateUserDto dto) {
        var userToken = this.findByToken(dto.token());
        this.validateTokenIsUsed(userToken);
        this.validateTokenIsExpired(userToken);
        this.burnToken(userToken);
    }
}

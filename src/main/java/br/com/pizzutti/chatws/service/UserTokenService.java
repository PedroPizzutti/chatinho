package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.model.UserToken;
import br.com.pizzutti.chatws.repository.UserTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    public UserTokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    public void burnToken(UserToken userToken) {
        userToken.setUsed(true);
        this.userTokenRepository.save(userToken);
    }

    public UserToken findByToken(String token) {
        return this.userTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token não encontrado!"));
    }

}

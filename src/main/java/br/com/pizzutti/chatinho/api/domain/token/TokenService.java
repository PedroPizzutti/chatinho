package br.com.pizzutti.chatinho.api.domain.token;

import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
public class TokenService extends FilterService<Token> {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expiration;

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        super();
        this.tokenRepository = tokenRepository;
    }

    public TokenDto generateToken(UserDto userDto) {
        var accessToken = this.generateAccessToken(userDto.id().toString());
        var refreshToken = this.generatedRefreshToken(userDto.id().toString());
        this.saveRefreshToken(userDto, refreshToken);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(TimeService.now().plusSeconds(expiration))
                .tokenType("Bearer")
                .build();
    }

    public String validateAccessToken(String token) {
        try {
            var jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setVerificationKey(this.getKey())
                    .build();

            var jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido!");
        }
    }

    public String validateRefreshToken(String token) {
        var spec = super.reset().filter("jwt", token, FilterOperationEnum.EQUAL).specification();
        var tokenBd = this.tokenRepository.findOne(spec)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido!"));
        this.tokenRepository.delete(tokenBd);

        try {
            var jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setVerificationKey(this.getReversedKey())
                    .build();

            var jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido!");
        }
    }

    private Key getKey() {
        var secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new HmacKey(secretBytes);
    }

    private Key getReversedKey() {
        var secretBytes = new StringBuilder(secret).reverse().toString().getBytes(StandardCharsets.UTF_8);
        return new HmacKey(secretBytes);
    }

    private String generateAccessToken(String subject) {
        try {
            var accessTokenClaims = new JwtClaims();
            accessTokenClaims.setSubject(subject);
            accessTokenClaims.setIssuer("chat-ws");
            accessTokenClaims.setIssuedAtToNow();
            accessTokenClaims.setExpirationTimeMinutesInTheFuture(expiration / 60f);

            var accessTokenJws = new JsonWebSignature();
            accessTokenJws.setPayload(accessTokenClaims.toJson());
            accessTokenJws.setAlgorithmHeaderValue("HS256");
            accessTokenJws.setKey(this.getKey());

            return accessTokenJws.getCompactSerialization();
        } catch (JoseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private String generatedRefreshToken(String subject) {
        try {
            var refreshTokenClaims = new JwtClaims();
            refreshTokenClaims.setSubject(subject);
            refreshTokenClaims.setGeneratedJwtId();
            refreshTokenClaims.setIssuer("chat-ws");
            refreshTokenClaims.setIssuedAtToNow();
            refreshTokenClaims.setExpirationTimeMinutesInTheFuture(expiration * 60f);

            var refreshTokenJWs = new JsonWebSignature();
            refreshTokenJWs.setPayload(refreshTokenClaims.toJson());
            refreshTokenJWs.setAlgorithmHeaderValue("HS256");
            refreshTokenJWs.setKey(this.getReversedKey());

            return refreshTokenJWs.getCompactSerialization();
        } catch (JoseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void saveRefreshToken(UserDto userDto, String refreshToken) {
        var token = Token.builder()
                .idOwner(userDto.id())
                .createdAt(TimeService.now())
                .jwt(refreshToken)
                .build();
        this.tokenRepository.save(token);
    }
}

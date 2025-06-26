package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.dto.UserCreatedDto;
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
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expiration;

    private Key getKey() {
        var secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new HmacKey(secretBytes);
    }

    public TokenDto generateToken(UserCreatedDto user) {
        try {
            var accessTokenClaims = new JwtClaims();
            accessTokenClaims.setSubject(user.login());
            accessTokenClaims.setIssuedAtToNow();
            accessTokenClaims.setExpirationTimeMinutesInTheFuture(expiration / 60f);

            var accessTokenJws = new JsonWebSignature();
            accessTokenJws.setPayload(accessTokenClaims.toJson());
            accessTokenJws.setAlgorithmHeaderValue("HS256");
            accessTokenJws.setKey(this.getKey());

            var refreshTokenClaims = new JwtClaims();
            refreshTokenClaims.setSubject(user.login());
            refreshTokenClaims.setIssuedAtToNow();
            refreshTokenClaims.setExpirationTimeMinutesInTheFuture(expiration * 60f);

            var refreshTokenJWs = new JsonWebSignature();
            refreshTokenJWs.setPayload(refreshTokenClaims.toJson());
            refreshTokenJWs.setAlgorithmHeaderValue("HS256");
            refreshTokenJWs.setKey(this.getKey());

            var accessToken = accessTokenJws.getCompactSerialization();
            var refreshToken = refreshTokenJWs.getCompactSerialization();

            return new TokenDto(accessToken, refreshToken, "Bearer", expiration);
        } catch (JoseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public String validateToken(String token) {
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
}

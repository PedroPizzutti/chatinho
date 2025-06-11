package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.TokenDto;
import br.com.pizzutti.chatws.model.User;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
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

    public TokenDto generateToken(User user) {
        try {
            var jwtClaims = new JwtClaims();
            jwtClaims.setSubject(user.getLogin());
            jwtClaims.setIssuedAtToNow();
            jwtClaims.setExpirationTimeMinutesInTheFuture(expiration / 60f);

            var jws = new JsonWebSignature();
            jws.setPayload(jwtClaims.toJson());
            jws.setAlgorithmHeaderValue("HS256");
            jws.setKey(this.getKey());

            var jwt = jws.getCompactSerialization();

            return new TokenDto(jwt, "Bearer", expiration);
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
        }
    }
}

package br.com.fiap.MottuSenseMvc.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import br.com.fiap.MottuSenseMvc.model.Token;
import br.com.fiap.MottuSenseMvc.model.User;
import br.com.fiap.MottuSenseMvc.model.UserRole;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenService {
    private Instant expiresAt = LocalDateTime.now().plusMinutes(120).toInstant(ZoneOffset.ofHours(-3));
    private Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(User user){
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
        String jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().toString())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(jwt, user.getEmail());
    }

    public User getUserFromToken(String jwt) {
        var jwtVerified = JWT.require(algorithm).build().verify(jwt);
        String role = jwtVerified.getClaim("role").asString();
        String email = jwtVerified.getClaim("email").asString();
        return User.builder()
                .id(Long.valueOf(jwtVerified.getSubject()))
                .email(email)
                .role(UserRole.valueOf(role))
                .build();
    }
}
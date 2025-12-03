package com.edu.utez.bibliotecadigital.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.edu.utez.bibliotecadigital.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties  jwtProperties;
    private final Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secret());;

    public String generateToken(String subject) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expirationMs());
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiry)
                .withIssuedAt(Instant.now())
                .withIssuer("books-api")
                .sign(algorithm);
    }

    public boolean isTokenValid(String token, String username) {
        try{
            final String tokenUsername = extractUsername(token);
            return (tokenUsername != null && tokenUsername.equals(username));

        } catch (JWTVerificationException e){
            return false;
        }
    }

    public @Nullable  String extractUsername(String token){
        try{
            return decodeToken(token).getSubject();
        } catch (JWTVerificationException e){
            return null;
        }
    }

    private DecodedJWT decodeToken(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("books-api")
                .build();

        return verifier.verify(token);
    }
}

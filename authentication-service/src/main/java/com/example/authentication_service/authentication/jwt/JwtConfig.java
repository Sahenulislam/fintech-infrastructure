package com.example.authentication_service.authentication.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;


@Component
public class JwtConfig {
    private final String SECRET_KEY = "secretKey";
    private final long EXPIRATION_TIME = 1000 * 60 * 15;


    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public long getExpiration() {
        return EXPIRATION_TIME;
    }
}

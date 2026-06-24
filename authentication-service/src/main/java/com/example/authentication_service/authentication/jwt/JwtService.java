package com.example.authentication_service.authentication.jwt;


import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateToken(String userName, Long userId){
        return Jwts.builder()
                .subject(userName)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtConfig.getExpiration()))
                .signWith(jwtConfig.getSigningKey())
                .compact();
    }
}

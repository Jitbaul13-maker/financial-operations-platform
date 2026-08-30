package com.finops.financial_operations_platform.security.service;

import com.finops.financial_operations_platform.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value(("${jwt.expiration}"))
    private long expiry;

    private SecretKey getKey(){
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        if (user.getCustomerId() != null) {
            claims.put("cust_id", user.getCustomerId());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getKey())
                .compact();
    }

    public Claims extractClaims(String token) {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
}

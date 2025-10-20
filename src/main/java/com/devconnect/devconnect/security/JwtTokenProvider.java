package com.devconnect.devconnect.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long validityInMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long validityInMs) {
        // Support Base64-encoded secrets (preferred) or raw string secrets.
        byte[] keyBytes = null;
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            if (decoded != null && decoded.length >= 32) {
                keyBytes = decoded;
            }
        } catch (Exception ignored) {
            // not valid base64 - fall back to raw bytes
        }

        if (keyBytes == null) {
            byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
            if (raw.length >= 32) {
                keyBytes = raw;
            } else {
                throw new IllegalArgumentException("The configured JWT secret is too short. Provide at least 256 bits (32 bytes) — use a Base64-encoded 32-byte key or a 32+ char secret.");
            }
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMs = validityInMs;
    }

    public String createToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("uid", userId);
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityInMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object uid = claims.get("uid");
        if (uid instanceof Integer) return ((Integer) uid).longValue();
        if (uid instanceof Long) return (Long) uid;
        return Long.parseLong(uid.toString());
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}

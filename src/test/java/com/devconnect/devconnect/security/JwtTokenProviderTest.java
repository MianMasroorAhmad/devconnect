package com.devconnect.devconnect.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    @Test
    void acceptsRaw32ByteSecret() {
        String secret = "0123456789ABCDEF0123456789ABCDEF"; // 32 chars
        JwtTokenProvider p = new JwtTokenProvider(secret, 3600000);
        String token = p.createToken(1L, "user");
        assertTrue(p.validateToken(token));
        assertEquals("user", p.getUsername(token));
    }

    @Test
    void acceptsBase64Secret() {
        // 32 bytes of 0x01 encoded as Base64
        String base64 = "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQE=";
        JwtTokenProvider p = new JwtTokenProvider(base64, 3600000);
        String token = p.createToken(2L, "alice");
        assertTrue(p.validateToken(token));
        assertEquals("alice", p.getUsername(token));
    }

    @Test
    void rejectsShortSecret() {
        String shortSecret = "short";
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new JwtTokenProvider(shortSecret, 3600000));
        assertTrue(ex.getMessage().contains("too short"));
    }
}

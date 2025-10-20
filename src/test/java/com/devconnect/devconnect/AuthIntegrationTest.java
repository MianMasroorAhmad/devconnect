package com.devconnect.devconnect;

import com.devconnect.devconnect.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void registerCreatesToken() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("itestuser");
        req.setEmail("itest@example.com");
        req.setPassword("Password123!");

        ResponseEntity<String> resp = rest.postForEntity("/api/auth/register", req, String.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).contains("token");
    }
}

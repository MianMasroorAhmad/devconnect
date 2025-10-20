package com.devconnect.devconnect;

import com.devconnect.devconnect.dto.RegisterRequest;
import com.devconnect.devconnect.dto.LoginRequest;
import com.devconnect.devconnect.dto.CreatePostRequest;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthEndToEndTest {

    @LocalServerPort
    private int port;

    @Test
    void registerLoginCreatePost() throws Exception {
        RestTemplate rest = new RestTemplate();
        String base = "http://localhost:" + port;

        // 1) Register
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("e2e_user");
        reg.setEmail("e2e@example.com");
        reg.setPassword("password123");

        ResponseEntity<String> regResp = rest.postForEntity(base + "/api/auth/register", reg, String.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2) Login
    LoginRequest login = new LoginRequest();
    login.setUsername("e2e_user");
    login.setPassword("password123");

        ResponseEntity<String> loginResp = rest.postForEntity(base + "/api/auth/login", login, String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).contains("token");

    // extract token from JSON body
    String body = loginResp.getBody();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(body);
    String token = node.get("token").asText();

        // 3) Call protected endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreatePostRequest post = new CreatePostRequest();
        post.setContent("E2E hello");

        HttpEntity<CreatePostRequest> req = new HttpEntity<>(post, headers);
        ResponseEntity<String> postResp = rest.postForEntity(base + "/api/posts", req, String.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}

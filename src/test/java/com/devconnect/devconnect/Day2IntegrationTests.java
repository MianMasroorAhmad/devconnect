package com.devconnect.devconnect;

import com.devconnect.devconnect.dto.CreatePostRequest;
import com.devconnect.devconnect.dto.CreateCommentRequest;
import com.devconnect.devconnect.dto.LoginRequest;
import com.devconnect.devconnect.dto.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Day2IntegrationTests {

    @LocalServerPort
    private int port;

    private String base() { return "http://localhost:" + port; }

    private String registerAndLogin(RestTemplate rest) throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("d2_user");
        reg.setEmail("d2@example.com");
        reg.setPassword("password123");
        rest.postForEntity(base() + "/api/auth/register", reg, String.class);

        LoginRequest login = new LoginRequest();
        login.setUsername("d2_user");
        login.setPassword("password123");
        ResponseEntity<String> loginResp = rest.postForEntity(base() + "/api/auth/login", login, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(loginResp.getBody());
        return node.get("token").asText();
    }

    @Test
    void commentLikeFollowAndNegativeAuth() throws Exception {
        RestTemplate rest = new RestTemplate();

        // create post with auth
        String token = registerAndLogin(rest);
        HttpHeaders auth = new HttpHeaders();
        auth.setBearerAuth(token);
        auth.setContentType(MediaType.APPLICATION_JSON);

        CreatePostRequest post = new CreatePostRequest();
        post.setContent("Day2 post");
        HttpEntity<CreatePostRequest> postReq = new HttpEntity<>(post, auth);
        ResponseEntity<String> postResp = rest.postForEntity(base() + "/api/posts", postReq, String.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Extract created post id from response body
        ObjectMapper mapper = new ObjectMapper();
        JsonNode postNode = mapper.readTree(postResp.getBody());
        Long createdPostId = postNode.get("id").asLong();

        // Create comment for the created post
        CreateCommentRequest comment = new CreateCommentRequest();
        comment.setText("Nice post");
        HttpEntity<CreateCommentRequest> commentReq = new HttpEntity<>(comment, auth);
        ResponseEntity<String> commentResp = rest.postForEntity(base() + "/api/posts/" + createdPostId + "/comments", commentReq, String.class);
        assertThat(commentResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Negative auth: missing token when liking (use actual post id)
        assertThatThrownBy(() -> {
            rest.postForEntity(base() + "/api/posts/" + createdPostId + "/like", null, String.class);
        }).isInstanceOf(HttpClientErrorException.class);
    }
}

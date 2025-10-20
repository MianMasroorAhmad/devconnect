package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.CreatePostRequest;
import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.service.PostService;
import com.devconnect.devconnect.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostService postService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PostService postService() {
            return Mockito.mock(PostService.class);
        }
    }

    @Test
    void createPostAuthenticated() throws Exception {
        // Mock security using try-with-resources so the static mock is closed after the test
        try (MockedStatic<SecurityUtils> security = Mockito.mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUserId).thenReturn(1L);

            CreatePostRequest req = new CreatePostRequest();
            req.setContent("Hello");

            Post p = new Post();
            p.setId(1L);
            p.setContent("Hello");
            p.setCreatedAt(LocalDateTime.now());

            Mockito.when(postService.create(any(CreatePostRequest.class), eq(1L))).thenReturn(p);

        mvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content("{\"content\":\"Hello\"}")
                .with(csrf()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }
    }
}

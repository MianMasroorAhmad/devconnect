package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.CreateCommentRequest;
import com.devconnect.devconnect.model.Comment;
import com.devconnect.devconnect.service.CommentService;
import com.devconnect.devconnect.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = CommentController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentService commentService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CommentService commentService() {
            return Mockito.mock(CommentService.class);
        }
    }

    @Test
    void createCommentAuthenticated() throws Exception {
        try (MockedStatic<SecurityUtils> security = Mockito.mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUserId).thenReturn(1L);

            Mockito.when(commentService.create(eq(1L), any(CreateCommentRequest.class), eq(1L)))
                    .thenAnswer(inv -> {
                        Comment c = new Comment();
                        c.setId(1L);
                        c.setText(((CreateCommentRequest) inv.getArgument(1)).getText());
                        c.setCreatedAt(LocalDateTime.now());
                        return c;
                    });

            mvc.perform(post("/api/posts/1/comments").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Nice\"}")
                            .with(csrf()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }
    }
}

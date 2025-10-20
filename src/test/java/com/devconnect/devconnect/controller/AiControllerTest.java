package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.AiSuggestionDto;
import com.devconnect.devconnect.service.ai.AIService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AiController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class AiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AIService aiService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AIService aiService() {
            return Mockito.mock(AIService.class);
        }
    }

    @Test
    void suggestReturnsSuggestions() throws Exception {
        Mockito.when(aiService.suggestForPost(eq(1L), Mockito.isNull()))
                .thenReturn(List.of(new AiSuggestionDto("Tip 1"), new AiSuggestionDto("Tip 2")));

        mvc.perform(get("/api/ai/suggest/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].suggestion").value("Tip 1"))
                .andExpect(jsonPath("$[1].suggestion").value("Tip 2"));
    }
}

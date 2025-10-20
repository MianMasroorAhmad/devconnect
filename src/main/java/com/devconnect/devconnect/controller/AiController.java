package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.AiSuggestionDto;
import com.devconnect.devconnect.security.SecurityUtils;
import com.devconnect.devconnect.service.ai.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AIService aiService;

    public AiController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/suggest/{postId}")
    public ResponseEntity<List<AiSuggestionDto>> suggest(@PathVariable Long postId) {
        Long userId = null;
        try {
            userId = SecurityUtils.getCurrentUserId();
        } catch (Exception ignored) {
            // If not authenticated, we still allow suggestions but with null user context
        }
        List<AiSuggestionDto> suggestions = aiService.suggestForPost(postId, userId);
        return ResponseEntity.ok(suggestions);
    }
}

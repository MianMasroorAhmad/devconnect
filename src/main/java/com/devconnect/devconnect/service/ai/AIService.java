package com.devconnect.devconnect.service.ai;

import com.devconnect.devconnect.dto.AiSuggestionDto;

import java.util.List;

public interface AIService {
    List<AiSuggestionDto> suggestForPost(Long postId, Long userId);
}

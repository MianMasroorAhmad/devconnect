package com.devconnect.devconnect.service.ai;

import com.devconnect.devconnect.dto.AiSuggestionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockAIService implements AIService {

    @Override
    public List<AiSuggestionDto> suggestForPost(Long postId, Long userId) {
        List<AiSuggestionDto> out = new ArrayList<>();
        out.add(new AiSuggestionDto("Clarify the problem statement to include expected input and output."));
        out.add(new AiSuggestionDto("Add an example showing a failing case and how to reproduce."));
        out.add(new AiSuggestionDto("Consider adding tests for edge cases such as empty input."));
        return out;
    }
}

package com.devconnect.devconnect.dto;

public class AiSuggestionDto {
    private String suggestion;

    public AiSuggestionDto() {}

    public AiSuggestionDto(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}

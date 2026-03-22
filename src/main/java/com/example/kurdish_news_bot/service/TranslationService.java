package com.example.kurdish_news_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    @Value("${cohere.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final String COHERE_API_URL = "https://api.cohere.ai/v1/chat";

    public TranslationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String translateToKurdish(String textToTranslate) {
        if (textToTranslate == null || textToTranslate.trim().isEmpty()) {
            return "";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "command-a-03-2025");
            requestBody.put("message", textToTranslate);

            String prompt = "You are an expert professional news translator. Your task is to translate the provided text from its original language (whether English, Russian, Arabic, or any other) into highly accurate Central Kurdish (Sorani) using the standard Kurdish-Arabic script. \n" +
                    "Strict Guidelines:\n" +
                    "1. Maintain a formal, objective, and journalistic tone suitable for a major news agency.\n" +
                    "2. Focus on the core meaning and natural sentence flow; avoid rigid, word-for-word translation.\n" +
                    "3. Ensure perfect Sorani grammar and spelling.\n" +
                    "4. Output ONLY the final translated Sorani text. Absolutely no conversational filler, no introductions, and no extra punctuation.";

            requestBody.put("preamble", prompt);
            requestBody.put("temperature", 0.3);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    COHERE_API_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("text")) {
                return (String) responseBody.get("text");
            }

        } catch (Exception e) {
            System.out.println("❌ کێشە لە وەرگێڕانی Cohere: " + e.getMessage());
        }

        return textToTranslate;
    }
}
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

    @Value("${gemma.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final String GEMMA_API_URL = "https://api.groq.com/openai/v1/chat/completions";

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
            requestBody.put("model", "gemma-4");
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
                    GEMMA_API_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("text")) {
                return (String) responseBody.get("text");
            }

        } catch (Exception e) {
            System.out.println("❌ کێشە لە وەرگێڕانی Gemma 4: " + e.getMessage());
        }

        return textToTranslate;
    }

    public int chooseMostImportantNews(String numberedTitles){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gemma-4");

            requestBody.put("message", numberedTitles);

            String prompt = "You are an expert Chief Editor of a global news agency. Review the following numbered list of news headlines. Select the single most important and impactful global news story. CRITICAL RULE: You must reply with ONLY the number of the selected headline. Do not add any text, explanation, or punctuation.";
            requestBody.put("preamble", prompt);
            requestBody.put("temperature", 0.1);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(GEMMA_API_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("text")) {
                String aiResponse = (String) responseBody.get("text");

                return Integer.parseInt(aiResponse.trim());
            }

        } catch (Exception e) {
            System.out.println("❌ کێشە لە هەڵبژاردنی هەواڵ لە Gemma 4: " + e.getMessage());
        }

        return 0;
    }

    public String analyzeNews(String fullContent) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gemma-4");

            requestBody.put("message", fullContent);

            String prompt = "You are a professional journalist. Read the following news article carefully. Provide a captivating and objective summary and analysis of this news. CRITICAL RULES: 1. The final output MUST be in Central Kurdish (Sorani)...";
            requestBody.put("preamble", prompt);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(GEMMA_API_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("text")) {
                return (String) responseBody.get("text");
            }

        } catch (Exception e) {
            System.out.println("❌ کێشە لە شیکاری Gemma 4: " + e.getMessage());
        }
        return "";
    }
}
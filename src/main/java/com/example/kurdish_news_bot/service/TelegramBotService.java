package com.example.kurdish_news_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class TelegramBotService {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.chat.id}")
    private String chatId;


    public void sendMessage(String text){

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        HashMap<String, String> payload = new HashMap<>();

        payload.put("chat_id", chatId);
        payload.put("text", text);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject(url, payload, String.class);
    }
}

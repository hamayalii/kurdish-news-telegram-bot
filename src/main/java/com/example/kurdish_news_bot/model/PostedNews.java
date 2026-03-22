package com.example.kurdish_news_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PostedNews {

    @Id
    private String url;

    public PostedNews() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

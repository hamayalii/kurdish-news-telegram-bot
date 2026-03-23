package com.example.kurdish_news_bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PostedNews {

    @Id
    private String url;

    private String title;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String kurdishContent;

    public PostedNews() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKurdishContent() {
        return kurdishContent;
    }

    public void setKurdishContent(String kurdishContent) {
        this.kurdishContent = kurdishContent;
    }
}

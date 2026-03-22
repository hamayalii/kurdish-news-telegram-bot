package com.example.kurdish_news_bot.repository;

import com.example.kurdish_news_bot.model.PostedNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<PostedNews, String> {
}

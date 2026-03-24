package com.example.kurdish_news_bot.repository;

import com.example.kurdish_news_bot.model.PostedNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repository extends JpaRepository<PostedNews, String> {

    List<PostedNews> findBySourceName(String sourceName);

    long count();
}

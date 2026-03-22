package com.example.kurdish_news_bot.service;

import com.example.kurdish_news_bot.model.NewsArticle;

import java.util.List;

public interface NewsProvider {

    List<NewsArticle> fetchNews();
}

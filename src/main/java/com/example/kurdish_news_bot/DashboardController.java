package com.example.kurdish_news_bot;

import com.example.kurdish_news_bot.model.NewsArticle;
import com.example.kurdish_news_bot.model.PostedNews;
import com.example.kurdish_news_bot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController{
    @Autowired
    Repository repository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model){

        return null;
    }
}

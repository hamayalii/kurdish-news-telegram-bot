package com.example.kurdish_news_bot;

import com.example.kurdish_news_bot.model.NewsArticle;
import com.example.kurdish_news_bot.model.PostedNews;
import com.example.kurdish_news_bot.repository.Repository;
import com.example.kurdish_news_bot.service.NewsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController{
    @Autowired
    Repository repository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @RequestParam(required = false) String source){

        List<PostedNews> allNews;

        if(source != null && !source.trim().isEmpty()){
            allNews = repository.findBySourceName(source);
        }else {
            allNews = repository.findAll();
        }

        model.addAttribute("newsList", allNews);


        long totalNewsCount = repository.count();

        model.addAttribute("totalNews", totalNewsCount);

        model.addAttribute("todayNews", 5);


        return "dashboard";
    }
}

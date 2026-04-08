package com.example.kurdish_news_bot;

import com.example.kurdish_news_bot.model.PostedNews;
import com.example.kurdish_news_bot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController{
    @Autowired
    Repository repository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @RequestParam(required = false) String source,
                                @RequestParam(required = false) Boolean today){

        List<PostedNews> allNews;

        if (Boolean.TRUE.equals(today)) {
            allNews = repository.findByPostedAt(LocalDate.now());
        } else if (source != null && !source.trim().isEmpty()) {
            allNews = repository.findBySourceName(source);
        } else {
            allNews = repository.findAll();
        }

        model.addAttribute("newsList", allNews);

        long totalNewsCount = repository.count();
        long todayNewsCount = repository.countByPostedAt(LocalDate.now());

        model.addAttribute("totalNews", totalNewsCount);
        model.addAttribute("todayNews", todayNewsCount);

        return "dashboard";
    }
}
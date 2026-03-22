package com.example.kurdish_news_bot;

import com.example.kurdish_news_bot.model.NewsArticle;
import com.example.kurdish_news_bot.service.NewsProvider;
import com.example.kurdish_news_bot.service.TranslationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.example.kurdish_news_bot.bot.NewsTelegramBot;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class KurdishNewsBotApplication implements CommandLineRunner{

    private final NewsProvider newsProvider;

    public KurdishNewsBotApplication(NewsProvider newsProvider) {
        this.newsProvider = newsProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(KurdishNewsBotApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(TranslationService translationService) {
        return args -> {
            System.out.println("--------- دەستپێکردنی تاقیکردنەوەی Groq ---------");

            String englishText = "Hello my friend! I am very happy that the code is finally working.";
            System.out.println("دەقی ئینگلیزی: " + englishText);

            String kurdishText = translationService.translateToKurdish(englishText);
            System.out.println("وەرگێڕانی کوردی: " + kurdishText);

            System.out.println("-------------------------------------------------");
        };
    }
    @Bean
    public TelegramBotsApi telegramBotsApi(NewsTelegramBot bot) throws Exception {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        System.out.println("پەنجەمۆر: بۆتەکە بە سەرکەوتوویی بەسترایەوە بە تێلیگرامەوە!");
        return api;
    }

    @Override
    public void run(String... args) throws Exception {
        List<NewsArticle> articles = newsProvider.fetchNews();

        for (NewsArticle article : articles){
            System.out.println(article.getTitle());
        }
    }
}
package com.example.kurdish_news_bot.bot;

import com.example.kurdish_news_bot.model.NewsArticle;
import com.example.kurdish_news_bot.service.NewsProvider;
import com.example.kurdish_news_bot.service.TranslationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Component
public class NewsTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String botName;

    private final NewsProvider newsProvider;

    private final TranslationService translationService;

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.kurdish_news_bot.repository.Repository repository;
    public NewsTelegramBot(@Value("${telegram.bot.token}") String botToken, NewsProvider newsProvider, TranslationService translationService) {
        super(botToken);
        this.newsProvider = newsProvider;
        this.translationService = translationService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println("📥 نامەیەک لە تێلیگرامەوە هات: " + messageText);

            if (messageText.equals("/news")) {

                System.out.println("✅ فەرمانی news وەرگیرا، ئێستا دەست دەکەم بە هێنانی هەواڵ...");

                SendMessage waitMessage = new SendMessage(String.valueOf(chatId), "⏳ خەریکی هێنان و وەرگێڕانی تازەترین هەواڵەکانم لە ڕێگەی Cohere AI، تکایە چەند چرکەیەک چاوەڕێ بکە...");
                try {
                    execute(waitMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Thread(() -> {
                        broadcast();
                }).start();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void broadcast(){
            List<NewsArticle> articles = newsProvider.fetchNews();

            for (int i = 0; i < Math.min(25, articles.size()); i++) {
                NewsArticle article = articles.get(i);

                if (repository.existsById(article.getUrl())){
                    System.out.println("⚠️ ئەم هەواڵە پێشتر پۆست کراوە: " + article.getTitle());
                    continue;
                }

                String linkToCheck = article.getUrl().toLowerCase();
                String titleToCheck = article.getTitle().toLowerCase();

                boolean isSportNews = linkToCheck.contains("sport") ||
                        linkToCheck.contains("رياضة") ||
                        titleToCheck.contains("sport") ||
                        titleToCheck.contains("football") ||
                        titleToCheck.contains("soccer") ||
                        titleToCheck.contains("مێسی") ||
                        titleToCheck.contains("رۆناڵدۆ") ||
                        titleToCheck.contains("basketball") ||
                        titleToCheck.contains("hockey") ||
                        titleToCheck.contains("fifa");

                if (isSportNews){
                    System.out.println("⚽ هەواڵێکی وەرزشی پشتگوێ خرا: " + article.getTitle());
                    continue;
                }

                String kurdishTitle;
                String kurdishDesc;

                if (article.getUrl().contains("rudaw.net")){
                    kurdishTitle = article.getTitle();
                    kurdishDesc = article.getFullContent();
                }else {
                    kurdishTitle = translationService.translateToKurdish(article.getTitle());
                    kurdishDesc = translationService.translateToKurdish(article.getFullContent());
                }
                String fullMessage = "📰 *" + kurdishTitle + "*\n\n📝 " + kurdishDesc + "\n\n🔗 [بۆ خوێندنەوەی تەواوی بابەتەکە کلیک لێرە بکە](" + article.getUrl() + ")";

                try {
                    if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                        org.telegram.telegrambots.meta.api.methods.send.SendPhoto photo = new org.telegram.telegrambots.meta.api.methods.send.SendPhoto();
                        photo.setChatId(String.valueOf("@testt7393923"));

                        try {
                            java.net.URL url = new java.net.URL(article.getImageUrl());
                            java.net.URLConnection connection = url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                            java.io.InputStream imageStream = connection.getInputStream();
                            photo.setPhoto(new org.telegram.telegrambots.meta.api.objects.InputFile(imageStream, "news_image.jpg"));
                        } catch (Exception e) {
                            System.out.println("کێشە لە داونلۆدی وێنە: " + e.getMessage());
                            photo.setPhoto(new org.telegram.telegrambots.meta.api.objects.InputFile(article.getImageUrl()));
                        }

                        if (fullMessage.length() > 1000){

                            int lastDot = fullMessage.lastIndexOf(".", 1000);
                            int lastSpace = fullMessage.lastIndexOf(" ",1000);
                            int lastComma = fullMessage.lastIndexOf("،",1000);

                            if(lastDot != -1 && lastDot > 900){
                                fullMessage = fullMessage.substring(0,(lastDot + 1));
                            }else if(lastComma != -1 && lastComma > 900){
                                fullMessage = fullMessage.substring(0, lastComma);
                            }else{
                                fullMessage = fullMessage.substring(0, lastSpace) + "...";
                            }
                        }
                        photo.setCaption(fullMessage);
                        photo.setParseMode("Markdown");
                        execute(photo);
                        com.example.kurdish_news_bot.model.PostedNews savedNews = new com.example.kurdish_news_bot.model.PostedNews();
                        savedNews.setUrl(article.getUrl());
                        repository.save(savedNews);
                        System.out.println("✅ هەواڵێکی نوێ خەزن کرا لە داتابەیس.");
                        Thread.sleep(2000);
                    } else {
                        SendMessage msg = new SendMessage(String.valueOf("@testt7393923"), fullMessage);
                        msg.setParseMode("Markdown");
                        execute(msg);
                        com.example.kurdish_news_bot.model.PostedNews savedNews = new com.example.kurdish_news_bot.model.PostedNews();
                        savedNews.setUrl(article.getUrl());
                        repository.save(savedNews);
                        System.out.println("✅ هەواڵێکی نوێ خەزن کرا لە داتابەیس.");
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    System.out.println("❌ کێشە لە ناردنی هەواڵ بۆ تێلیگرام: " + e.getMessage());
                }
            }
        }
    }


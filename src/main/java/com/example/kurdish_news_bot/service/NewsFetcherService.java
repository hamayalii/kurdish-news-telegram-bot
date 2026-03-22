package com.example.kurdish_news_bot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsFetcherService {

    public void fetchArabicNews(){
        String rssUrl = "https://arabic.rt.com/rss/";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(rssUrl,String.class);

        org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());

        org.jsoup.nodes.Element firstItem = document.selectFirst("item");

        if (firstItem != null) {
            String title = firstItem.select("title").text();
            String link = firstItem.select("link").text();

            System.out.println("ناونیشانی هەواڵ: " + title);
            System.out.println("لینکی هەواڵ: " + link);
        } else {
            System.out.println("هیچ هەواڵێک نەدۆزرایەوە!");
        }
    }
}

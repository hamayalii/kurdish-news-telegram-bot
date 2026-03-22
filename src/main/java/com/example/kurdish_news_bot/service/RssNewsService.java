package com.example.kurdish_news_bot.service;

import com.example.kurdish_news_bot.model.NewsArticle;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.springframework.stereotype.Service;

import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssNewsService implements  NewsProvider{


    @Override
    public List<NewsArticle> fetchNews() {

        List<NewsArticle> articleList = new ArrayList<>();


        String[] urlString = {"https://feeds.bbci.co.uk/news/world/rss.xml"
                , "https://www.aljazeera.com/xml/rss/all.xml"
                , "http://rss.cnn.com/rss/edition.rss"
                , "https://rss.dw.com/rdf/rss-en-world"
                , "https://russian.rt.com/rss"
        };
        for (String singleUrl : urlString) {

            try{

            URL feedUrl = new URL(singleUrl);

            SyndFeedInput input = new SyndFeedInput();

            SyndFeed feed = input.build(new XmlReader(feedUrl));

            int count = 0;
            for (SyndEntry entry : feed.getEntries()) {

                if(count == 5){
                    break;
                }
                NewsArticle myArticle = new NewsArticle();
                myArticle.setImageUrl(null);

                if (!entry.getEnclosures().isEmpty()) {
                    myArticle.setImageUrl(entry.getEnclosures().get(0).getUrl());
                } else {
                    for (org.jdom2.Element element : entry.getForeignMarkup()) {
                        if ("thumbnail".equals(element.getName()) && "media".equals(element.getNamespacePrefix())) {
                            myArticle.setImageUrl(element.getAttributeValue("url"));
                            break;
                        }
                    }
                }

                myArticle.setTitle(entry.getTitle());
                myArticle.setUrl(entry.getLink());

                try {
                    org.jsoup.nodes.Document doc = org.jsoup.Jsoup.connect(entry.getLink())
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                            .timeout(5000)
                            .get();

                    if (myArticle.getImageUrl() == null){
                        String hiddenImage = doc.select("meta[property=og:image]").attr("content");

                        if (hiddenImage != null && !hiddenImage.isEmpty()){
                            myArticle.setImageUrl(hiddenImage);
                        }
                    }

                    org.jsoup.select.Elements paragraphs = doc.select("p");
                    StringBuilder fullText = new StringBuilder();
                    for (org.jsoup.nodes.Element p : paragraphs) {
                        if(p.text().length() < 40){
                            continue;
                        }

                        fullText.append(p.text()).append("\n");
                        if (fullText.length() > 3000) break;
                    }
                    myArticle.setFullContent(fullText.toString());
                } catch (Exception e) {
                    System.out.println("نەتوانرا دەقی درێژ بهێنرێت: " + e.getMessage());

                    myArticle.setFullContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                }
                articleList.add(myArticle);
                count++;
            }
        }catch (Exception e){
                System.out.println("کێشەیەك هەیە لە هێنانی هەواڵەکان: "+ e.getMessage());

            }
    }
        return articleList;
    }
}

package com.meindonsa.tech_watch.source.service;

import com.meindonsa.tech_watch.article.Article;
import com.meindonsa.tech_watch.source.Source;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RssFeedService {

    public List<Article> fetchRssFeed(Source source) {
        List<Article> articles = new ArrayList<>();

        try {
            URL feedUrl = new URL(source.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            for (SyndEntry entry : feed.getEntries()) {
                Article article = new Article();
                article.setTitle(entry.getTitle());
                article.setUrl(entry.getLink());
                article.setSource(source);
                article.setFetchedDate(LocalDateTime.now());

                // Contenu
                if (entry.getDescription() != null) {
                    article.setContent(entry.getDescription().getValue());
                }

                // Date de publication
                if (entry.getPublishedDate() != null) {
                    article.setPublishedDate(
                            entry.getPublishedDate()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime());
                }

                // Auteur
                if (entry.getAuthor() != null) {
                    article.setAuthor(entry.getAuthor());
                }

                // Image
                if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
                    article.setImageUrl(entry.getEnclosures().get(0).getUrl());
                }

                articles.add(article);
            }

        } catch (Exception e) {
            log.error("Erreur lors de la récupération du flux RSS: {}", source.getUrl(), e);
        }

        return articles;
    }
}

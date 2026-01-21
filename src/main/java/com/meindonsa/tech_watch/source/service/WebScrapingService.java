package com.meindonsa.tech_watch.source.service;

import com.meindonsa.tech_watch.article.Article;
import com.meindonsa.tech_watch.source.Source;
import com.meindonsa.toolbox.exception.FunctionalException;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WebScrapingService {

    public List<Article> scrapeWebsite(Source source) {
        List<Article> articles = new ArrayList<>();

        try {
            Document doc =
                    Jsoup.connect(source.getUrl()).userAgent("Mozilla/5.0").timeout(10000).get();

            Elements articleElements = doc.select(source.getArticleSelector());

            for (Element element : articleElements) {
                Article article = new Article();
                article.setSource(source);
                article.setFetchedDate(LocalDateTime.now());

                // Titre
                Element titleElement = element.selectFirst(source.getTitleSelector());
                if (titleElement != null) {
                    article.setTitle(titleElement.text());
                }

                // URL
                Element linkElement = element.selectFirst("a[href]");
                if (linkElement != null) {
                    String url = linkElement.absUrl("href");
                    article.setUrl(url);
                }

                // Contenu
                if (source.getContentSelector() != null) {
                    Element contentElement = element.selectFirst(source.getContentSelector());
                    if (contentElement != null) {
                        article.setContent(contentElement.text());
                    }
                }

                // Date
                if (source.getDateSelector() != null) {
                    Element dateElement = element.selectFirst(source.getDateSelector());
                    if (dateElement != null) {
                        article.setPublishedDate(parseDate(dateElement.text()));
                    }
                }

                articles.add(article);
            }

        } catch (IOException e) {
            // log.error("Erreur lors du scraping: {}", source.getUrl(), e);
            throw new FunctionalException("Erreur lors du scraping: " + source.getUrl());
        }

        return articles;
    }

    private LocalDateTime parseDate(String dateStr) {
        // Implémentez votre logique de parsing de date
        // selon les formats rencontrés
        return LocalDateTime.now();
    }
}

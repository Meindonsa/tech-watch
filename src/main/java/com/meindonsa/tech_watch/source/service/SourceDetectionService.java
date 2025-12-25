package com.meindonsa.tech_watch.source.service;

import com.meindonsa.tech_watch.source.SourceDetectionResult;
import com.meindonsa.tech_watch.source.SourceType;
import com.meindonsa.toolbox.exception.FunctionalException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
public class SourceDetectionService {

    private static final List<String> RSS_INDICATORS =
            List.of("/feed", "/rss", "/atom", ".xml", "/feeds");

    public SourceType detectSourceType(String url) {
        url = sanitizeUrl(url);
        try {
            if (isLikelyRssUrl(url)) {
                if (isValidRssFeed(url)) {
                    return SourceType.RSS;
                }
            }

            String feedUrl = findRssFeedUrl(url);
            if (feedUrl != null) {
                log.info("Flux RSS trouvé: {}", feedUrl);
                return SourceType.RSS;
            }

            return SourceType.SCRAPING;

        } catch (Exception e) {
            log.error("Erreur lors de la détection: {}", url, e);
            return SourceType.SCRAPING;
        }
    }

    public SourceDetectionResult detectSource(String url) {
        url = sanitizeUrl(url);
        SourceDetectionResult result = new SourceDetectionResult();
        result.setOriginalUrl(url);

        try {
            // 1. Vérifier si l'URL est directement un flux RSS
            if (isLikelyRssUrl(url) && isValidRssFeed(url)) {
                result.setType(SourceType.RSS);
                result.setFeedUrl(url);
                result.setDetectionMethod("URL directe");
                return result;
            }

            // 2. Chercher un flux RSS dans les métadonnées de la page
            String feedUrl = findRssFeedUrl(url);
            if (feedUrl != null) {
                result.setType(SourceType.RSS);
                result.setFeedUrl(feedUrl);
                result.setDetectionMethod("Balise <link> dans le HTML");
                return result;
            }

            // 3. Essayer des URLs communes
            String commonFeedUrl = tryCommonRssPaths(url);
            if (commonFeedUrl != null) {
                result.setType(SourceType.RSS);
                result.setFeedUrl(commonFeedUrl);
                result.setDetectionMethod("URL commune (/feed, /rss, etc.)");
                return result;
            }

            // 4. Pas de flux trouvé, utiliser le scraping
            result.setType(SourceType.SCRAPING);
            result.setFeedUrl(url);
            result.setDetectionMethod("Aucun flux RSS trouvé");

        } catch (Exception e) {
            log.error("Erreur lors de la détection: {}", url, e);
            result.setType(SourceType.SCRAPING);
            result.setFeedUrl(url);
            result.setDetectionMethod("Erreur - défaut au scraping");
        }

        return result;
    }

    private String sanitizeUrl(String url) {
        if (url == null) {
            return null;
        }
        return url.replace("\"", "").trim();
    }

    private String tryCommonRssPaths(String url) {
        List<String> commonPaths =
                List.of(
                        "/feed",
                        "/rss",
                        "/atom",
                        "/feed.xml",
                        "/rss.xml",
                        "/atom.xml",
                        "/index.xml",
                        "/feeds/posts/default" // Blogger
                        );

        try {
            URL baseUrl = new URL(url);
            String base = baseUrl.getProtocol() + "://" + baseUrl.getHost();

            for (String path : commonPaths) {
                String testUrl = base + path;
                if (isValidRssFeed(testUrl)) {
                    return testUrl;
                }
            }
        } catch (MalformedURLException e) {
            String error = String.format("URL invalide: %s", url);
            throw new FunctionalException(error);
        }

        return null;
    }

    private boolean isLikelyRssUrl(String url) {
        String lowerUrl = url.toLowerCase();
        return RSS_INDICATORS.stream().anyMatch(lowerUrl::contains);
    }

    private boolean isValidRssFeed(String url) {
        try {
            URL feedUrl = new URL(url);
            SyndFeedInput input = new SyndFeedInput();
            input.build(new XmlReader(feedUrl));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String findRssFeedUrl(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();

            Elements links = doc.select("link[type*=rss], link[type*=atom], link[type*=xml]");

            for (Element link : links) {
                String href = link.absUrl("href");
                if (!href.isEmpty() && isValidRssFeed(href)) {
                    return href;
                }
            }

            Elements alternateLinks = doc.select("link[rel=alternate]");
            for (Element link : alternateLinks) {
                String type = link.attr("type");
                if (type.contains("rss") || type.contains("atom") || type.contains("xml")) {
                    String href = link.absUrl("href");
                    if (!href.isEmpty() && isValidRssFeed(href)) {
                        return href;
                    }
                }
            }

        } catch (IOException e) {
            final String error = String.format("Erreur lors de la recherche de flux RSS: %s", url);
            throw new FunctionalException(error);
        }

        return null;
    }
}

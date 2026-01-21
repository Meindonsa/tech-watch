package com.meindonsa.tech_watch.source.service;

import com.meindonsa.tech_watch.article.Article;
import com.meindonsa.tech_watch.article.ArticleService;
import com.meindonsa.tech_watch.config.GlobalAppConfig;
import com.meindonsa.tech_watch.shared.PaginatedRequest;
import com.meindonsa.tech_watch.shared.Utils;
import com.meindonsa.tech_watch.source.CreateSourceView;
import com.meindonsa.tech_watch.source.Source;
import com.meindonsa.tech_watch.source.SourceDao;
import com.meindonsa.tech_watch.source.SourceDetectionResult;
import com.meindonsa.tech_watch.source.SourceView;
import com.meindonsa.tech_watch.source.SourcesView;
import com.meindonsa.toolbox.exception.FunctionalException;
import com.meindonsa.toolbox.utils.Functions;
import com.meindonsa.toolbox.utils.MapperUtils;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SourceService implements ISourceService {

    @Autowired private SourceDao sourceDao;
    @Autowired private GlobalAppConfig globalAppConfig;
    @Autowired private ArticleService articleService;
    @Autowired private RssFeedService rssFeedService;
    @Autowired private WebScrapingService webScrapingService;
    @Autowired private SourceDetectionService detectionService;

    @PostConstruct
    private void initSources() {
        List<CreateSourceView> views = globalAppConfig.getSources();
        if (Functions.isNullOrEmpty(views)) return;
        for (CreateSourceView view : views) {
            createSource(view);
        }
    }

    @Scheduled(fixedDelay = 3600000) // Toutes les heures
    public void aggregateNews() {
        log.info("Démarrage de l'agrégation des news");
        List<Source> sources = sourceDao.findByActive(true);

        for (Source source : sources) {
            aggregateSourceNews(source);
        }

        log.info("Agrégation terminée");
    }

    private void aggregateSourceNews(Source source) {
        try {
            List<Article> articles = articleService.saveNewArticles(fetchArticles(source));
            if (articles.isEmpty()) return;
            source.getArticles().addAll(articles);
            source.setLastFetch(LocalDateTime.now());
            sourceDao.save(source);

        } catch (Exception e) {
            throw new FunctionalException("Erreur lors de l'agrégation pour: " + source.getName());
        }
    }

    private Source aggregateNewSourceNews(Source source) {
        try {
            List<Article> articles = articleService.saveNewArticles(fetchArticles(source));
            if (articles.isEmpty()) return source;
            source.getArticles().addAll(articles);
            source.setLastFetch(LocalDateTime.now());
            return source;

        } catch (Exception e) {
            throw new FunctionalException("Erreur lors de l'agrégation pour: " + source.getName());
        }
    }

    private List<Article> fetchArticles(Source source) {
        return switch (source.getType()) {
            case RSS -> rssFeedService.fetchRssFeed(source);
            case SCRAPING -> webScrapingService.scrapeWebsite(source);
        };
    }

    public Source retrieveByFunctionalID(String fid) {
        Source source = sourceDao.findByFid(fid);
        if (source == null) throw new FunctionalException("Source inexistante");
        return source;
    }

    @Override
    public void deleteSource(String fid) {
        Source source = retrieveByFunctionalID(fid);
        sourceDao.delete(source);
    }

    @Override
    public void enableSource(String fid) {
        Source source = retrieveByFunctionalID(fid);
        if (source.isActive()) return;
        source.setActive(true);
        sourceDao.save(source);
    }

    @Override
    public void disableSource(String fid) {
        Source source = retrieveByFunctionalID(fid);
        if (!source.isActive()) return;
        source.setActive(false);
        sourceDao.save(source);
    }

    @Transactional
    @Override
    public void createSource(CreateSourceView view) {
        SourceDetectionResult detection = detectionService.detectSource(view.getUrl());
        Source source = new Source();
        source.setActive(true);
        source.setName(view.getName());
        source.setType(detection.getType());
        source.setUrl(detection.getOriginalUrl());
        source.setFeedUrl(detection.getFeedUrl());
        if (findByFeeUrl(source.getFeedUrl()) != null) {
            log.info("L'url: {} est déjà utilisé !", source.getUrl());
            return;
        }
        if (detection.isFeedFound()) {
            source.setUrl(detection.getFeedUrl());
            log.info("Flux RSS détecté pour {}: {}", view.getName(), detection.getFeedUrl());
        } else {
            log.info("Pas de flux RSS, scraping configuré pour: {}", view.getName());
        }

        sourceDao.save(aggregateNewSourceNews(source));
    }

    @Override
    public void updateSource(SourceView view) {
        Source source = retrieveByFunctionalID(view.getFid());
        source.setName(view.getName());
        sourceDao.save(source);
    }

    @Override
    public SourceView retrieveSource(String fid) {
        return MapperUtils.map(retrieveByFunctionalID(fid), SourceView.class);
    }

    @Override
    public SourceDetectionResult detectSource(String url) {
        return detectionService.detectSource(url);
    }

    @Override
    public SourcesView retrieveSources(PaginatedRequest request) {
        Pageable pageable = Utils.getPaging(request);
        Page<Source> sources = sourceDao.search(Utils.getString(request.getSearchKey()), pageable);
        return response(sources);
    }

    private SourcesView response(Page<Source> page) {
        SourcesView response = new SourcesView();
        response.setObjects(MapperUtils.mapAll(page.getContent(), SourceView.class));
        response.setIndex(page.getNumber());
        response.setSize(page.getSize());
        response.setTotal(page.getTotalElements());
        return response;
    }

    private Source findByUrl(String url) {
        return sourceDao.findByUrl(url);
    }

    private Source findByFeeUrl(String feedUrl) {
        return sourceDao.findByFeedUrl(feedUrl);
    }
}

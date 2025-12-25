package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.shared.PaginatedRequest;
import com.meindonsa.tech_watch.shared.Utils;
import com.meindonsa.tech_watch.source.Source;
import com.meindonsa.tech_watch.source.service.SourceService;
import com.meindonsa.toolbox.utils.Functions;
import com.meindonsa.toolbox.utils.MapperUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleService implements IArticleService {
    @Autowired private ArticleDao articleDao;
    @Autowired private SourceService sourceService;

    public void saveNewArticles(List<Article> articles) {
        for (Article article : articles) {
            if (!articleDao.existsByUrl(article.getUrl())) {
                articleDao.save(article);
                log.info("Nouvel article sauvegardé: {}", article.getTitle());
            }
        }
    }

    @Override
    public ArticlesView retrieveArticles(PaginatedRequest request) {
        Source source =
                Functions.isNullOrEmpty(request.getFid())
                        ? null
                        : sourceService.retrieveByFunctionalID(request.getFid());
        Pageable pageable = Utils.getPaging(request);
        Page<Article> articles =
                articleDao.search(Utils.getString(request.getSearchKey()), source, pageable);
        return response(articles);
    }

    private ArticlesView response(Page<Article> page) {
        ArticlesView response = new ArticlesView();
        response.setObjects(MapperUtils.mapAll(page.getContent(), ArticleView.class));
        response.setIndex(page.getNumber());
        response.setSize(page.getSize());
        response.setTotal(page.getTotalElements());
        return response;
    }
}

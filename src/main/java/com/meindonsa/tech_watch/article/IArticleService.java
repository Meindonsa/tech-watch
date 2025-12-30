package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.shared.PaginatedRequest;

public interface IArticleService {

    ArticleView retrieveArticle(String fid);

    ArticlesView retrieveArticles(PaginatedRequest request);
}

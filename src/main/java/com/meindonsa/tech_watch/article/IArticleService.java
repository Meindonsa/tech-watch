package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.shared.PaginatedRequest;

public interface IArticleService {
    ArticlesView retrieveArticles(PaginatedRequest request);
}

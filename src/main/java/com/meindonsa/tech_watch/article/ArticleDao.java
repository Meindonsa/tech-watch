package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.source.Source;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleDao extends JpaRepository<Article, Long> {

    boolean existsByUrl(String url);

    @Query(
            "SELECT a FROM Article a WHERE (:key IS NULL OR LOWER(a.title) LIKE "
                    + " CONCAT('%',:key,'%')) AND (:source IS NULL OR a.source = :source)")
    Page<Article> search(String key, Source source, Pageable pageable);
}

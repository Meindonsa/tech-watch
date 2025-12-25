package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.shared.PaginatedRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/article")
@CrossOrigin("*")
@Tag(name = "Article", description = "Article API")
public class ArticleController implements IArticleService {

    @Autowired private ArticleService articleService;

    @PostMapping("/articles")
    @Operation(summary = "Retrieve articles")
    @Override
    public ArticlesView retrieveArticles(@RequestBody PaginatedRequest request) {
        return articleService.retrieveArticles(request);
    }
}

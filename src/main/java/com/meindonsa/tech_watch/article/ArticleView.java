package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.source.SourceView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleView {
    private String title;

    private String content;

    private String url;

    private LocalDateTime publishedDate;

    private LocalDateTime fetchedDate;

    private SourceView source;

    private String imageUrl;
    private String author;
}

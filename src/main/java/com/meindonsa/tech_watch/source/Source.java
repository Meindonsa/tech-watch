package com.meindonsa.tech_watch.source;

import com.meindonsa.tech_watch.article.Article;
import com.meindonsa.tech_watch.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_source_fid",
                        columnNames = {"fid"})
        })
public class Source extends BaseEntity {

    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private SourceType type;

    private boolean active = true;

    // Pour le scraping
    private String articleSelector;
    private String titleSelector;
    private String contentSelector;
    private String dateSelector;

    private LocalDateTime lastFetch;

    @OneToMany(mappedBy = "source", orphanRemoval = true)
    private List<Article> articles;
}

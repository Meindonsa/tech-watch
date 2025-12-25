package com.meindonsa.tech_watch.article;

import com.meindonsa.tech_watch.shared.BaseEntity;
import com.meindonsa.tech_watch.source.Source;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_article_fid",
                    columnNames = {"fid"})
        })
public class Article extends BaseEntity {

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(unique = true)
    private String url;

    private LocalDateTime publishedDate;
    private LocalDateTime fetchedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_source"), nullable = false)
    private Source source;

    private String imageUrl;
    private String author;
}

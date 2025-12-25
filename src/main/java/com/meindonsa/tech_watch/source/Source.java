package com.meindonsa.tech_watch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SourceType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source extends BaseEntity {

    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private SourceType type; // RSS ou SCRAPING

    private Boolean active = true;

    // Pour le scraping
    private String articleSelector;
    private String titleSelector;
    private String contentSelector;
    private String dateSelector;

    private LocalDateTime lastFetch;
}

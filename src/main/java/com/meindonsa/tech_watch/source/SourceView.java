package com.meindonsa.tech_watch.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceView {

    private String fid;

    private String name;

    private String url;

    private SourceType type;

    private String dateSelector;

    private String titleSelector;

    private String articleSelector;

    private String contentSelector;

    private LocalDateTime lastFetch;
}

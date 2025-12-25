package com.meindonsa.tech_watch.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDetectionResult {

    private String originalUrl;
    private SourceType type;
    private String feedUrl;
    private String detectionMethod;
    private boolean feedFound;

    public boolean isFeedFound() {
        return type == SourceType.RSS && feedUrl != null;
    }
}

package com.meindonsa.tech_watch.source.service;

import com.meindonsa.tech_watch.shared.PaginatedRequest;
import com.meindonsa.tech_watch.source.SourceDetectionResult;
import com.meindonsa.tech_watch.source.SourceView;
import com.meindonsa.tech_watch.source.SourcesView;

public interface ISourceService {

    void deleteSource(String fid);

    void enableSource(String fid);

    void disableSource(String fid);

    void createSource(SourceView view);

    void updateSource(SourceView view);

    SourceView retrieveSource(String fid);

    SourceDetectionResult detectSource(String url);

    SourcesView retrieveSources(PaginatedRequest request);
}

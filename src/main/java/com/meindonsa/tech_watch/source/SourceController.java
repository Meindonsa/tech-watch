package com.meindonsa.tech_watch.source;

import com.meindonsa.tech_watch.shared.PaginatedRequest;
import com.meindonsa.tech_watch.source.service.ISourceService;
import com.meindonsa.tech_watch.source.service.SourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/source")
@CrossOrigin("*")
@Tag(name = "Source", description = "Source API")
public class SourceController implements ISourceService {

    @Autowired private SourceService sourceService;

    @DeleteMapping("/remove/{fid}")
    @Operation(description = "Delete source")
    @Override
    public void deleteSource(@PathVariable String fid) {
        sourceService.deleteSource(fid);
    }

    @PutMapping("/enable/{fid}")
    @Operation(description = "Enable source")
    @Override
    public void enableSource(@PathVariable String fid) {
        sourceService.enableSource(fid);
    }

    @PutMapping("/disable/{fid}")
    @Operation(description = "Disable source")
    @Override
    public void disableSource(@PathVariable String fid) {
        sourceService.disableSource(fid);
    }

    @PostMapping("/create")
    @Operation(description = "Create source")
    @Override
    public void createSource(@RequestBody CreateSourceView view) {
        sourceService.createSource(view);
    }

    @PutMapping("/upate")
    @Operation(description = "Update source")
    @Override
    public void updateSource(@RequestBody SourceView view) {
        sourceService.updateSource(view);
    }

    @GetMapping("/source/{fid}")
    @Operation(description = "Retrieve source")
    @Override
    public SourceView retrieveSource(@PathVariable String fid) {
        return sourceService.retrieveSource(fid);
    }

    @PostMapping("/detect")
    @Operation(description = "Detect source")
    @Override
    public SourceDetectionResult detectSource(@RequestBody String url) {
        return sourceService.detectSource(url);
    }

    @PostMapping
    @Operation(description = "Retreive Sources")
    @Override
    public SourcesView retrieveSources(@RequestBody PaginatedRequest request) {
        return sourceService.retrieveSources(request);
    }
}

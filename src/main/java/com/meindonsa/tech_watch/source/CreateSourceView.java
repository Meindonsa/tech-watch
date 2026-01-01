package com.meindonsa.tech_watch.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSourceView {
    private String fid;
    private String url;
    private String name;
}

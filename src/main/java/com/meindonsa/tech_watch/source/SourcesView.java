package com.meindonsa.tech_watch.source;

import com.meindonsa.toolbox.view.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourcesView extends PageInfo {
    private List<SourceView> objects;
}

package com.meindonsa.tech_watch.shared;

import com.meindonsa.toolbox.view.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedRequest extends PageInfo {
    private String fid;
    private String searchKey;
}

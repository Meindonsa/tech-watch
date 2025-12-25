package com.meindonsa.tech_watch.shared;

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
public class PaginatedResponse extends PageInfo {
    private List<Object> objects;
}

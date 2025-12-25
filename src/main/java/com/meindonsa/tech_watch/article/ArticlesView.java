package com.meindonsa.tech_watch.article;

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
public class ArticlesView extends PageInfo {
    private List<ArticleView> objects;
}

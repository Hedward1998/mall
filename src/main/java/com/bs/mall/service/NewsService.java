package com.bs.mall.service;


import com.bs.mall.entity.News;
import com.bs.mall.util.PageUtil;

import java.util.List;

public interface NewsService {
    boolean add(News news);
    boolean update(News news);
    News get(Integer news_id);
    List<News> getList(News news, PageUtil pageUtil);
    Integer getTotal(News news);
    
}

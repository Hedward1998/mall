package com.bs.mall.service.impl;

import com.bs.mall.dao.NewsMapper;
import com.bs.mall.entity.News;
import com.bs.mall.service.NewsService;
import com.bs.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("newsService")
public class NewsServiceImpl implements NewsService {
    
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<News> getList(News news, PageUtil pageUtil) {
        return newsMapper.select(news, pageUtil);
    }

    @Override
    public boolean add(News news) {
        return newsMapper.insertOne(news)>0;
    }

    @Override
    public boolean update(News news) {
        return newsMapper.updateOne(news)>0;
    }

    @Override
    public News get(Integer news_id) {
        return newsMapper.selectOne(news_id);
    }

    @Override
    public Integer getTotal(News news) {
        return newsMapper.selectTotal(news);
    }
}

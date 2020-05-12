package com.bs.mall.dao;

import com.bs.mall.entity.News;
import com.bs.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {
    Integer insertOne(@Param("news") News news);
    Integer updateOne(@Param("news") News news);
    List<News> select(@Param("news") News news, @Param("pageUtil") PageUtil pageUtil);
    News selectOne(@Param("news_id") Integer news_id);
    Integer selectTotal(@Param("news") News news);
    List<News> selectTitle(@Param("news") News news, @Param("pageUtil") PageUtil pageUtil);
}
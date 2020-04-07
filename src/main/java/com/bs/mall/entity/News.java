package com.bs.mall.entity;

import java.util.Date;

/**
 * 新闻类
 */
public class News {
    private Integer news_id/*新闻ID*/;
    
    private String news_title/*新闻标题*/;
    
    private String news_content/*新闻内容*/;
    
    private Date news_publish_date/*新闻发布日期*/;
    
    private Admin news_publish_person/*新闻发布人*/;

    public News(Integer news_id, String news_title, String news_content, Date news_publish_date, Admin news_publish_person) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_content = news_content;
        this.news_publish_date = news_publish_date;
        this.news_publish_person = news_publish_person;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public Date getNews_publish_date() {
        return news_publish_date;
    }

    public void setNews_publish_date(Date news_publish_date) {
        this.news_publish_date = news_publish_date;
    }

    public Admin getNews_publish_person() {
        return news_publish_person;
    }

    public void setNews_publish_person(Admin news_publish_person) {
        this.news_publish_person = news_publish_person;
    }
}

package com.bs.mall.entity;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 新闻公告类
 */
public class News {
    private Integer news_id/*新闻公告ID*/;
    
    private String news_title/*新闻公告标题*/;
    
    private String news_content/*新闻公告内容*/;
    
    private Date news_publish_date/*新闻公告发布日期*/;
    
    private Admin news_publish_person_id/*新闻发布人ID*/;
    
    private String news_publish_person_name/*新闻公告发布人姓名*/;
    
    private Byte news_status/*新闻公告发布状态*/;

    public News(){ }

    public News(String news_title, String news_content, Date news_publish_date, Admin news_publish_person_id, Byte news_status) {
        this.news_title = news_title;
        this.news_content = news_content;
        this.news_publish_date = news_publish_date;
        this.news_publish_person_id = news_publish_person_id;
        this.news_status = news_status;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public News setNews_id(Integer news_id) {
        this.news_id = news_id;
        return this;
    }

    public String getNews_title() {
        return news_title;
    }

    public News setNews_title(String news_title) {
        this.news_title = news_title;
        return this;
    }

    public String getNews_content() {
        return news_content;
    }

    public News setNews_content(String news_content) {
        this.news_content = news_content;
        return this;
    }

    public String getNews_publish_date() {
        if (news_publish_date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            return sdf.format(news_publish_date);
        }
        return null;
    }

    public News setNews_publish_date(Date news_publish_date) {
        this.news_publish_date = news_publish_date;
        return this;
    }
    
    public String getNews_publish_person_name() {
        return news_publish_person_name;
    }

    public News setNews_publish_person_name(String news_publish_person_name) {
        this.news_publish_person_name = news_publish_person_name;
        return this;
    }

    public Admin getNews_publish_person_id() {
        return news_publish_person_id;
    }

    public News setNews_publish_person_id(Admin news_publish_person_id) {
        this.news_publish_person_id = news_publish_person_id;
        return this;
    }
    
    public Byte getNews_status() {
        return news_status;
    }
    
    public News setNews_status(Byte news_status) {
        this.news_status = news_status;
        return this;
    }
}

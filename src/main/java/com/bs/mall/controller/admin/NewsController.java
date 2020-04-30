package com.bs.mall.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台 新闻公告管理
 */
@Controller
public class NewsController {
    
    //添加新闻公告
    @ResponseBody
    @RequestMapping(value = "news/new", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addNews(){
        return "";
    }
}

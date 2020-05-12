package com.bs.mall.controller.fore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bs.mall.controller.BaseController;
import com.bs.mall.entity.News;
import com.bs.mall.service.NewsService;
import com.bs.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 前台-新闻公告信息
 */

@Controller
public class ForeNewsController extends BaseController {
    @Autowired 
    private NewsService newsService;
    
    //前台-查询已发布的新闻公告列表(时间倒序)
    @RequestMapping(value = "news", method = RequestMethod.GET)
    public String goNewsManagePage(HttpSession session, Map<String, Object> map) {
        logger.info("获取前10条已发布新闻公告");
        PageUtil pageUtil = new PageUtil(0, 10);
        News news = new News().setNews_status((byte)1);
        List<News> newsList = newsService.getList(news, pageUtil);
        map.put("newsList", newsList);
        logger.info("获取已发布新闻公告总数量");
        Integer newsCount = newsService.getTotal(news);
        map.put("newsCount", newsCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(newsCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到前台-已发布新闻公告列表-ajax方式");
        return "fore/news";
    }

    //前台-条件查询已发布的新闻公告列表-ajax方式(时间倒序)
    @ResponseBody
    @RequestMapping(value = "news/{index}/{count}", method = RequestMethod.GET)
    public String getNewsBySearch(@RequestParam(required = false) String news_title/* 新闻公告标题 */,
                                  @PathVariable Integer index/* 页数 */,
                                  @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        
        //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
        news_title = (news_title != null) && (!"".equals(news_title)) ? URLDecoder.decode(news_title, "UTF-8") : null;
        //封装查询条件
        News news = new News().setNews_title(news_title).setNews_status((byte)1);
        JSONObject object = new JSONObject();
        logger.info("条件获取已发布的第{}页的{}条新闻公告", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<News> newsList = newsService.getList(news, pageUtil);
        object.put("newsList", JSONArray.parseArray(JSON.toJSONString(newsList)));
        logger.info("条件获取已发布的新闻总数量");
        Integer newsCount = newsService.getTotal(news);
        object.put("newsCount", newsCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(newsCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);
        return object.toJSONString();
    }
    
    //前台-新闻公告详情
    @RequestMapping(value = "news/{pid}", method = RequestMethod.GET)
    public String goToDetailsPage(Map<String, Object> map, @PathVariable Integer pid/* 新闻公告ID */) {
        logger.info("获取news_id为{}的新闻公告信息", pid);
        News news = newsService.get(pid);
        map.put("news", news);
        logger.info("转到前台-新闻公告详情");
        return "fore/newsDetails";
    }
    
}

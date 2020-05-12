package com.bs.mall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bs.mall.controller.BaseController;
import com.bs.mall.entity.Admin;
import com.bs.mall.entity.News;
import com.bs.mall.service.AdminService;
import com.bs.mall.service.LastIDService;
import com.bs.mall.service.NewsService;
import com.bs.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 后台 新闻公告管理
 */
@Controller
public class NewsController extends BaseController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private NewsService newsService;
    
    @Autowired
    private LastIDService lastIDService;
    
    //后台-新闻公告查询
    @RequestMapping(value = "admin/news", method = RequestMethod.GET)
    public String goNewsManagePage(HttpSession session, Map<String, Object> map) {
        logger.info("获取前10条新闻公告");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<News> newsList = newsService.getList(null, pageUtil);
        map.put("newsList", newsList);
        logger.info("获取新闻公告总数量");
        Integer newsCount = newsService.getTotal(null);
        map.put("newsCount", newsCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(newsCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-新闻公告列表-ajax方式");
        return "admin/newsManagePage";
    }
    
    //后台-条件查询新闻公告-ajax(时间倒序,可根据标题模糊查询)
    @ResponseBody
    @RequestMapping(value = "admin/news/{index}/{count}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getNewsBySearch(@RequestParam(required = false) String news_title/* 新闻公告标题 */,
                            @RequestParam(required = false) Byte[] news_status_array/* 状态数组 */,
                            @PathVariable Integer index/* 页数 */,
                            @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        
        //判断新闻公告选择状态,若未选择或者都选择，则查询全部数据
        Byte news_status = (news_status_array != null) && (news_status_array.length == 1) ? news_status_array[0] : null;
        //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
        news_title = (news_title != null) && (!"".equals(news_title)) ? URLDecoder.decode(news_title, "UTF-8") : null;
        //封装查询条件
        News news = new News().setNews_title(news_title).setNews_status(news_status);
        JSONObject object = new JSONObject();
        logger.info("条件获取第{}页的{}条新闻公告", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<News> newsList = newsService.getList(news, pageUtil);
        object.put("newsList", JSONArray.parseArray(JSON.toJSONString(newsList)));
        logger.info("条件获取新闻总数量");
        Integer newsCount = newsService.getTotal(news);
        object.put("newsCount", newsCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(newsCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);
        return object.toJSONString();
    }
    
    //后台-新闻详情页
    @RequestMapping(value = "admin/news/{pid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer pid/* 新闻ID */) {
        logger.info("获取news_id为{}的新闻信息", pid);
        News news = newsService.get(pid);
        map.put("news", news);
        logger.info("转到后台管理-新闻详情-ajax方式");
        return "admin/include/newsDetails";
    }

    //后台-添加新闻公告
    @RequestMapping(value = "admin/news/new", method = RequestMethod.POST)
    public String addNews(HttpSession session,
                          @RequestParam String news_title/* 新闻公告标题 */,
                          @RequestParam String news_content/* 内容 */){
        logger.info("获取管理员信息");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        logger.info("整合新闻公告信息");
        News news = new News()
                .setNews_title(news_title)
                .setNews_content(news_content)
                .setNews_publish_date(new Date())
                .setNews_publish_person_id(new Admin().setAdmin_id(Integer.parseInt(adminId.toString())))
                .setNews_status((byte)1);
        logger.info("添加新闻公告");
        boolean yn = newsService.add(news);
        if (!yn) {
            logger.info("新闻公告添加失败！事务回滚");
            throw new RuntimeException();
        }
        int new_id = lastIDService.selectLastID();
        logger.info("新闻公告添加成功！新发布的新闻公告ID为：{}", new_id);

        logger.info("转到后台-新闻公告列表");
        return "redirect:/news/0/10";
    }
    
    //后台-更新新闻信息-ajax方式
    @ResponseBody
    @RequestMapping(value = "admin/news/{news_id}", method = RequestMethod.PUT,  produces = "application/json;charset=utf-8")
    public String updateNews(HttpSession session,
                             @RequestParam String news_title/* 新闻公告标题 */,
                             @RequestParam String news_content/* 新闻公告内容 */,
                             @RequestParam Byte news_status/* 新闻公告状态（1:true,发布；2:false,未发布,删除） */,
                             @PathVariable("news_id") Integer news_id/* 新闻公告ID */) {
        logger.info("获取管理员信息");
        JSONObject jsonObject = new JSONObject();
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        logger.info("整合新闻公告信息");
        News news = new News()
                .setNews_id(news_id)
                .setNews_title(news_title)
                .setNews_content(news_content)
                .setNews_publish_date(new Date())
                .setNews_publish_person_id(new Admin().setAdmin_id(Integer.parseInt(adminId.toString())))
                .setNews_status(news_status);
        logger.info("更新新闻公告");
        boolean yn = newsService.add(news);
        if (!yn) {
            logger.info("新闻公告更新失败！事务回滚");
            throw new RuntimeException();
        }
        int new_id = lastIDService.selectLastID();
        logger.info("新闻公告更新成功！更新的新闻公告ID为：{}", new_id);

        logger.info("转到后台-新闻公告列表");
        return "redirect:/news/0/10";
    }
}

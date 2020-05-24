package com.bs.mall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bs.mall.controller.BaseController;
import com.bs.mall.entity.Admin;
import com.bs.mall.entity.OrderGroup;
import com.bs.mall.service.*;
import com.bs.mall.util.OrderUtil;
import com.bs.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台管理-主页
 */
@Controller
public class AdminHomeController extends BaseController {
    @Resource(name = "adminService")
    private AdminService adminService;
    @Resource(name = "productOrderService")
    private ProductOrderService productOrderService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "userService")
    private UserService userService;
    @Autowired
    private LastIDService lastIDService;

    /**
     * 转到后台管理-主页
     * @param session session对象
     * @param map 前台传入的Map
     * @return 响应数据
     * @throws ParseException 转换异常
     */
    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) throws ParseException {
        logger.info("获取管理员信息");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        logger.info("获取统计信息");
        //产品总数
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        //用户总数
        Integer userTotal = userService.getTotal(null);
        //订单总数
        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        logger.info("获取图表信息");
        map.put("jsonObject", getChartData(null,null,7));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
        map.put("orderTotal", orderTotal);

        logger.info("转到后台管理-主页");
        return "admin/homePage";
    }

    /**
     * 转到后台管理-主页（ajax方式）
     * @param session session对象
     * @param map 前台传入的Map
     * @return 响应数据
     * @throws ParseException 转换异常
     */
    @RequestMapping(value = "admin/home", method = RequestMethod.GET)
    public String goToPageByAjax(HttpSession session, Map<String, Object> map) throws ParseException {
        logger.info("获取管理员信息");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        logger.info("获取统计信息");
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        Integer userTotal = userService.getTotal(null);
        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        logger.info("获取图表信息");
        map.put("jsonObject", getChartData(null, null,7));
        logger.info("获取图表信息");
        map.put("jsonObject", getChartData(null,null,7));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
        map.put("orderTotal", orderTotal);
        logger.info("转到后台管理-主页-ajax方式");
        return "admin/homeManagePage";
    }

    /**
     * 按日期查询图表数据（ajax方式）
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 响应数据
     * @throws ParseException 转换异常
     */
    @ResponseBody
    @RequestMapping(value = "admin/home/charts", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getChartDataByDate(@RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate) throws ParseException {
        if (beginDate != null && endDate != null) {
            //转换日期格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return getChartData(simpleDateFormat.parse(beginDate), simpleDateFormat.parse(endDate),7).toJSONString();
        } else {
            return getChartData(null, null,7).toJSONString();
        }
    }

    /**
     * 按日期获取图表数据
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @param days 天数
     * @return 图表数据的JSON对象
     * @throws ParseException 转换异常
     */
    private JSONObject getChartData(Date beginDate,Date endDate,int days) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat time2 = new SimpleDateFormat("MM/dd", Locale.UK);
        SimpleDateFormat timeSpecial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        //如果没有指定开始和结束日期
        if (beginDate == null || endDate == null) {
            //指定一周前的日期为开始日期
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1-days);
            beginDate = time.parse(time.format(cal.getTime()));
            //指定当前日期为结束日期
            cal = Calendar.getInstance();
            endDate = cal.getTime();
        } else {
            beginDate = time.parse(time.format(beginDate));
            endDate = timeSpecial.parse(time.format(endDate) + " 23:59:59");
        }
        logger.info("根据订单状态分类");
        //未付款订单数统计数组
        int[] orderUnpaidArray = new int[7];
        //未发货订单数统计叔祖
        int[] orderNotShippedArray = new int[7];
        //未确认订单数统计数组
        int[] orderUnconfirmedArray = new int[7];
        //交易成功订单数统计数组
        int[] orderSuccessArray = new int[7];
        //总交易订单数统计数组
        int[] orderTotalArray = new int[7];
        logger.info("从数据库中获取统计的订单集合数据");
        List<OrderGroup> orderGroupList = productOrderService.getTotalByDate(beginDate, endDate);
        //初始化日期数组
        JSONArray dateStr = new JSONArray(days);
        //按指定的天数进行循环
        for (int i = 0; i < days; i++) {
            //格式化日期串（MM/dd）并放入日期数组中
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            cal.add(Calendar.DATE, i);
            String formatDate = time2.format(cal.getTime());
            dateStr.add(formatDate);
            //该天的订单总数
            int orderCount = 0;
            //循环订单集合数据的结果集
            for(int j = 0; j < orderGroupList.size(); j++){
                OrderGroup orderGroup = orderGroupList.get(j);
                //如果该订单日期与当前日期一致
                if(orderGroup.getProductOrder_pay_date().equals(formatDate)){
                    //从结果集中移除数据
                    orderGroupList.remove(j);
                    //根据订单状态将统计结果存入对应的订单状态数组中
                    switch (orderGroup.getProductOrder_status()) {
                        case 0:
                            //未付款订单
                            orderUnpaidArray[i] = orderGroup.getProductOrder_count();
                            break;
                        case 1:
                            //未发货订单
                            orderNotShippedArray[i] = orderGroup.getProductOrder_count();
                            break;
                        case 2:
                            //未确认订单
                            orderUnconfirmedArray[i] = orderGroup.getProductOrder_count();
                            break;
                        case 3:
                            //交易成功订单
                            orderSuccessArray[i] = orderGroup.getProductOrder_count();
                            break;
                    }
                    //累加当前日期的订单总数
                    orderCount += orderGroup.getProductOrder_count();
                }
            }
            //将统计的订单总数存入总交易订单数统计数组
            orderTotalArray[i] = orderCount;
        }
        logger.info("返回结果集map");
        jsonObject.put("orderTotalArray", orderTotalArray);
        jsonObject.put("orderUnpaidArray", orderUnpaidArray);
        jsonObject.put("orderNotShippedArray", orderNotShippedArray);
        jsonObject.put("orderUnconfirmedArray", orderUnconfirmedArray);
        jsonObject.put("ordaaerSuccessArray", orderSuccessArray);
        jsonObject.put("dateStr",dateStr);
        return jsonObject;
    }

    //转到后台管理-管理员页-ajax
    @RequestMapping(value = "admin/admin", method = RequestMethod.GET)
    public String goUserManagePage(HttpSession session, Map<String, Object> map){
        logger.info("获取前十条管理员信息");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Admin> adminList = adminService.getList(null, null, pageUtil);
        map.put("adminList", adminList);
        logger.info("获取管理员总数量");
        Integer adminCount = adminService.getTotal(null);
        map.put("adminCount", adminCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(adminCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-管理员页-ajax方式");
        return "admin/adminManagePage";
    }

    //转到后台管理-管理员详情页-ajax
    @RequestMapping(value = "admin/admin/{aid}", method = RequestMethod.GET)
    public String getUserById(HttpSession session, Map<String,Object> map, @PathVariable Integer aid/* 管理员ID */){
        logger.info("获取admin_id为{}的管理员信息",aid);
        Admin admin = adminService.get(null, aid);
        map.put("admin",admin);
        logger.info("转到后台管理-管理员详情页-ajax方式");
        return "admin/include/adminDetails";
    }

    //按条件查询管理员-ajax
    @ResponseBody
    @RequestMapping(value = "admin/admin/{index}/{count}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getUserBySearch(@RequestParam(required = false) String admin_name/* 管理员名称 */,
                                  @RequestParam(required = false) String orderBy/* 排序字段 */,
                                  @RequestParam(required = false,defaultValue = "true") Boolean isDesc/* 是否倒序 */,
                                  @PathVariable Integer index/* 页数 */,
                                  @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        if (admin_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            admin_name = "".equals(admin_name) ? null : URLDecoder.decode(admin_name, "UTF-8");
        }
        if (orderBy != null && "".equals(orderBy)) {
            orderBy = null;
        }
        OrderUtil orderUtil = null;
        if (orderBy != null) {
            logger.info("根据{}排序，是否倒序:{}",orderBy,isDesc);
            orderUtil = new OrderUtil(orderBy, isDesc);
        }

        JSONObject object = new JSONObject();
        logger.info("按条件获取第{}页的{}条管理员", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Admin> adminList = adminService.getList(admin_name, orderUtil, pageUtil);
        object.put("adminList", JSONArray.parseArray(JSON.toJSONString(adminList)));
        logger.info("按条件获取管理员总数量");
        Integer adminCount = adminService.getTotal(admin_name);
        object.put("adminCount", adminCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(adminCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }
    
    //转到后台管理-管理员添加页-ajax
    @RequestMapping(value = "admin/admin/new",method = RequestMethod.GET)
    public String goToAddPage(){
        logger.info("转到后台管理-管理员添加页-ajax方式");
        return "admin/include/adminAdd";
    }

    //管理员添加-ajax
    @ResponseBody
    @RequestMapping(value = "admin/admin/add",method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addAdmin(@RequestParam String admin_name/* 管理员账户名称 */,
                           @RequestParam String admin_password/* 管理员登录密码 */,
                           @RequestParam String admin_nickname/* 管理员昵称 */,
                           @RequestParam String admin_profile_picture_src/*管理员头像路径*/){
        JSONObject jsonObject = new JSONObject();
        //检查管理员账号是否重复
        if (adminService.get(admin_name, null) != null) {
            jsonObject.put("success", false);
            jsonObject.put("message", "管理员账号重复！");
            return jsonObject.toJSONString();
        }
        logger.info("整合管理员信息");
        //如果未设置密码着默认密码1234
        admin_password = (admin_password == null || "".equals(admin_password)) ? "1234" : admin_password;
        admin_profile_picture_src = (admin_profile_picture_src == null || "".equals(admin_profile_picture_src)) ? null : admin_profile_picture_src.substring(admin_profile_picture_src.lastIndexOf("/") + 1);
        Admin admin = new Admin()
                .setAdmin_name(admin_name)
                .setAdmin_password(admin_password)
                .setAdmin_nickname(admin_nickname)
                .setAdmin_profile_picture_src(admin_profile_picture_src);
        logger.info("添加管理员信息");
        boolean yn = adminService.add(admin);
        if (!yn) {
            jsonObject.put("success", false);
            jsonObject.put("message", "新增失败，请重试！");
        } else {
            int admin_id = lastIDService.selectLastID();
            logger.info("管理员添加成功！新增管理员ID为：" + admin_id);
            jsonObject.put("success", true);
            jsonObject.put("admin_id", admin_id);
        }
        return jsonObject.toJSONString();
    }
    
    //删除管理员-ajax
    @ResponseBody
    @RequestMapping(value = "admin/admin/del",method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
    public String deleteAdmin(@RequestParam Integer admin_id/* 管理员ID */){
        JSONObject jsonObject = new JSONObject();
        logger.info("删除管理员，id为{}", admin_id);
        boolean yn = adminService.delete(admin_id);
        if (!yn) {
            jsonObject.put("success", false);
        } else {
            jsonObject.put("success", true);
        }
        return jsonObject.toJSONString();
    }
}
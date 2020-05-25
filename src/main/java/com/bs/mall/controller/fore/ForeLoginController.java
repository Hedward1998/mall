package com.bs.mall.controller.fore;

import com.alibaba.fastjson.JSONObject;
import com.bs.mall.controller.BaseController;
import com.bs.mall.entity.User;
import com.bs.mall.service.UserService;
import com.bs.mall.util.Md5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登陆页
 */
@Controller
public class ForeLoginController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;

    //转到商城前台-登录页
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("转到商城前台-登录页");
        return "fore/loginPage";
    }

    //登陆验证-ajax
    @ResponseBody
    @RequestMapping(value = "login/doLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String checkLogin(HttpSession session, @RequestParam String username, @RequestParam String password) {
        logger.info("用户验证登录");
        JSONObject object = new JSONObject();
        User user = userService.login(new User().setUser_name(username).setUser_phone(username));
        if (user != null) {
            if (Md5Util.verify(password, null, user.getUser_password())) {
                logger.info("登录验证成功，用户ID传入会话");
                session.setAttribute("userId", user.getUser_id());
                object.put("success",true);
            } else {
                logger.info("用户登录-密码错误");
                object.put("message", "密码错误！");
                object.put("success",false);
            }
        } else {
            logger.info("用户登录-用户不存在");
            object.put("message", "不存在此账号！");
            object.put("success",false);
        }
        return object.toJSONString();
    }

    //退出当前账号
    @RequestMapping(value = "login/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        Object o = session.getAttribute("userId");
        if (o != null) {
            session.removeAttribute("userId");
            session.invalidate();
            logger.info("登录信息已清除，返回用户登录页");
        }
        return "redirect:/login";
    }
}

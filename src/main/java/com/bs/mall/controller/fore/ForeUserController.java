package com.bs.mall.controller.fore;

import com.alibaba.fastjson.JSONObject;
import com.bs.mall.controller.BaseController;
import com.bs.mall.entity.Address;
import com.bs.mall.entity.User;
import com.bs.mall.service.AddressService;
import com.bs.mall.service.UserService;
import com.bs.mall.util.Md5Util;
import com.bs.mall.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户信息管理
 */
@Controller
public class ForeUserController extends BaseController{
    @Resource(name = "addressService")
    private AddressService addressService;
    @Resource(name="userService")
    private UserService userService;

    //转到商城前台-用户详情页
    @RequestMapping(value = "userDetails", method = RequestMethod.GET)
    public String goToUserDetail(HttpSession session, Map<String,Object> map){
        logger.info("检查用户是否登录");
        Object userId = checkUser(session);
        if (userId != null) {
            logger.info("获取用户信息");
            User user = userService.get(Integer.parseInt(userId.toString()));
            map.put("user", user);

            logger.info("获取用户所在地区级地址");
            String districtAddressId = user.getUser_address().getAddress_areaId();
            Address districtAddress = addressService.get(districtAddressId);
            logger.info("获取市级地址信息");
            Address cityAddress = addressService.get(districtAddress.getAddress_regionId().getAddress_areaId());
            logger.info("获取其他地址信息");
            List<Address> addressList = addressService.getRoot();
            List<Address> cityList = addressService.getList(
                    null,cityAddress.getAddress_regionId().getAddress_areaId()
            );
            List<Address> districtList = addressService.getList(null,cityAddress.getAddress_areaId());

            map.put("addressList", addressList);
            map.put("cityList", cityList);
            map.put("districtList", districtList);
            map.put("addressId", cityAddress.getAddress_regionId().getAddress_areaId());
            map.put("cityAddressId", cityAddress.getAddress_areaId());
            map.put("districtAddressId", districtAddressId);
            return  "fore/userDetails";
        } else {
            return "redirect:/login";
        }
    }
    //商城前台-用户更换头像
    @ResponseBody
    @RequestMapping(value = "user/uploadUserHeadImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public  String uploadUserHeadImage(@RequestParam MultipartFile file, HttpSession session
    ){
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名：{}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/userProfilePicture/" + fileName;
        logger.info("文件上传路径：{}", filePath);
        JSONObject jsonObject = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传成功！");
            jsonObject.put("success", true);
            jsonObject.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败！");
            e.printStackTrace();
            jsonObject.put("success", false);
        }
        return jsonObject.toJSONString();
    }
    
    //商城前台-用户详情更新
    @RequestMapping(value="user/update",method=RequestMethod.POST,produces ="application/json;charset=utf-8")
    public String userUpdate(HttpSession session, Map<String,Object> map,
                             @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
                             @RequestParam(value = "user_realname") String user_realname  /*真实姓名*/,
                             @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
                             @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
                             @RequestParam(value = "user_phone") String user_phone /*用户电话*/,
                             @RequestParam(value = "user_address") String user_address  /*用户所在地 */,
                             @RequestParam(value = "user_profile_picture_src", required = false)
                                         String user_profile_picture_src /* 用户头像*/,
                             @RequestParam(value = "user_password") String user_password/* 用户密码 */
    ) throws ParseException, UnsupportedEncodingException {
        logger.info("检查用户是否登录");
        User user;
        Object userId = checkUser(session);
        if (userId != null) {
            logger.info("获取用户信息");
            user = userService.get(Integer.parseInt(userId.toString()));
            map.put("user", user);
        } else {
            return "redirect:/login";
        }
        if (!user.getUser_phone().equals(user_phone)) {
            logger.info("修改了电话号码,验证电话号码是否存在");
            int count = userService.getTotal(new User().setUser_phone(user_phone));
            if (count > 0) {
                logger.info("用户电话已存在，返回错误信息!");
                JSONObject object = new JSONObject();
                object.put("success", false);
                object.put("message", "电话号码已存在，请重新输入！");
                return object.toJSONString();
            }
        }
        logger.info("创建用户对象");
        if (user_profile_picture_src != null && "".equals(user_profile_picture_src)) {
            user_profile_picture_src = null;
        }
        User userUpdate = new User()
                .setUser_id(Integer.parseInt(userId.toString()))
                .setUser_nickname(user_nickname)
                .setUser_realname(user_realname)
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_phone(user_phone)
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_profile_picture_src(user_profile_picture_src);
        //如果修改了密码，才传入数据库
        if (!user.getUser_password().equals(user_password)) {
            userUpdate.setUser_password(Md5Util.md5(user_password, null));
        }
        logger.info("执行修改");
        if (userService.update(userUpdate)){
             logger.info("修改成功!跳转到用户详情页面");
             return "redirect:/userDetails";
         }
         throw new RuntimeException();
    }

    //商城前台-跳转忘记密码页面
    @RequestMapping(value="forgetPwd", method = RequestMethod.GET)
    public String forgetPwd() {
        return "fore/forgetPwd";
    }
    

    //商城前台-忘记密码-密码重置 ajax
    @ResponseBody
    @RequestMapping(value="forgetPwd",method=RequestMethod.POST,produces ="application/json;charset=utf-8")
    public String forgetPwd(@RequestParam(value = "user_name") String user_name  /*用户名 */,
                            @RequestParam(value = "user_phone") String user_phone /*用户电话*/,
                            @RequestParam(value = "user_password") String user_password  /*用户密码*/) throws ParseException {
        logger.info("验证密码符合规范");  // 4~11位数字、字母，必须包含数字和字母
        if (Util.validData("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{4,11}$", user_password)) {
            logger.info("密码不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "密码格式错误！");
            return object.toJSONString();
        }
        logger.info("验证用户名是否存在");
        User user = userService.login(new User().setUser_name(user_name));
        if (user == null) {
            logger.info("重置密码的用户名不存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "用户名不存在，请重新输入！");
            return object.toJSONString();
        }
        logger.info("验证电话号码是否存在");
        if (!user_phone.equals(user.getUser_phone())) {
            logger.info("重置密码的用户电话输入不正确，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "电话号码错误，请重新输入！");
            return object.toJSONString();
        }
        logger.info("用户：" + user_name + "重置密码");
        if (userService.forgetPassword(user_name, Md5Util.md5(user_password, null))) {
            logger.info("修改成功!跳转到登录页面！");
            JSONObject object = new JSONObject();
            object.put("success", true);
            object.put("message", "重置密码成功！");
            return object.toJSONString();
        } else {
            throw new RuntimeException();
        }
    }
}

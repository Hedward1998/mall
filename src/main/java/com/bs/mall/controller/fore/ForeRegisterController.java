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

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 注册页
 */
@Controller
public class ForeRegisterController extends BaseController{
    @Resource(name = "addressService")
    private AddressService addressService;
    @Resource(name="userService")
    private UserService userService;

    //转到商城前台-用户注册页
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String goToPage(Map<String,Object> map) {
        String addressId = "110000";
        String cityAddressId = "110100";
        logger.info("获取省份信息");
        List<Address> addressList = addressService.getRoot();
        logger.info("获取addressId为{}的市级地址信息", addressId);
        List<Address> cityAddress = addressService.getList(null, addressId);
        logger.info("获取cityAddressId为{}的区级地址信息", cityAddressId);
        List<Address> districtAddress = addressService.getList(null, cityAddressId);
        map.put("addressList", addressList);
        map.put("cityList", cityAddress);
        map.put("districtList", districtAddress);
        map.put("addressId", addressId);
        map.put("cityAddressId", cityAddressId);
        logger.info("转到前台-用户注册页");
        return "fore/register";
    }
    
    //商城前台-用户注册-ajax
    @ResponseBody
    @RequestMapping(value = "register/doRegister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String register(
            @RequestParam(value = "user_name") String user_name  /*用户名 */,
            @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
            @RequestParam(value = "user_password") String user_password  /*用户密码*/,
            @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
            @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
            @RequestParam(value = "user_phone") String user_phone /*用户电话*/,
            @RequestParam(value = "user_address") String user_address  /*用户所在地 */
    ) throws ParseException {
        logger.info("验证用户名符合规范");  // 4~11位数字、中英文、下划线或组合，不可重复
        if (Util.validData("^[\\u4e00-\\u9fa5a-zA-Z0-9_]{4,11}$", user_name)) {
            logger.info("用户名不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "用户名格式错误！");
            return object.toJSONString();
        }
        logger.info("验证密码符合规范");  // 4~11位数字、字母，必须包含数字和字母
        if (Util.validData("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{4,11}$", user_password)) {
            logger.info("密码不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "密码格式错误！");
            return object.toJSONString();
        }
        logger.info("验证昵称符合规范");  // 4~11位数字、中英文、下划线或组合
        if (Util.validData("^[\\u4e00-\\u9fa5a-zA-Z0-9_]{4,11}$", user_nickname)) {
            logger.info("昵称不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "昵称格式错误！");
            return object.toJSONString();
        }
        logger.info("验证性别是否符合规范");
        if (Util.isValidGender(user_gender)) {
            logger.info("性别不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "性别格式错误！");
            return object.toJSONString();
        }
        logger.info("验证电话号码是否符合规范");
        if (Util.isValidPhone(user_phone)) {
            logger.info("电话号码不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "电话号码格式错误！");
            return object.toJSONString();
        }
        logger.info("验证用户名是否存在");
        Integer count = userService.getTotal(new User().setUser_name(user_name));
        if (count > 0) {
            logger.info("用户名已存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "用户名已存在，请重新输入！");
            return object.toJSONString();
        }
        logger.info("验证出生日期是否符合规范");
        if (Util.validBirthday(user_birthday)) {
            logger.info("出生日期不符合规范，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "出生日期格式错误！");
            return object.toJSONString();
        }
        logger.info("验证电话号码是否存在");
        count = userService.getTotal(new User().setUser_phone(user_phone));
        if (count > 0) {
            logger.info("用户电话已存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("message", "电话号码已存在，请重新输入！");
            return object.toJSONString();
        }
        logger.info("创建用户对象");
        User user = new User()
                .setUser_name(user_name)
                .setUser_nickname(user_nickname)
                .setUser_password(Md5Util.md5(user_password, null))
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_phone(user_phone)
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_homeplace(new Address().setAddress_areaId("130000"));
        logger.info("用户注册");
        if (userService.add(user)) {
            logger.info("注册成功");
            JSONObject object = new JSONObject();
            object.put("success", true);
            object.put("message", "注册成功！");
            return object.toJSONString();
        } else {
            throw new RuntimeException();
        }
    }
}

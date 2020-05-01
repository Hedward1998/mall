package com.bs.mall.service;

import com.bs.mall.entity.User;
import com.bs.mall.util.OrderUtil;
import com.bs.mall.util.PageUtil;

import java.util.List;

public interface UserService {
    boolean add(User user);
    boolean update(User user);
    boolean resetPassword(String user_name, String password);

    List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil);
    User get(Integer user_id);
    User login(User user);
    Integer getTotal(User user);
}

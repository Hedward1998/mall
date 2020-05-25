package com.bs.mall.service;

import com.bs.mall.entity.Admin;
import com.bs.mall.util.OrderUtil;
import com.bs.mall.util.PageUtil;

import java.util.List;

public interface AdminService {
    boolean add(Admin admin);
    boolean update(Admin admin);
    boolean delete(Integer admin_id);
    boolean resetPassword(Admin admin);

    List<Admin> getList(String admin_name, OrderUtil orderUtil, PageUtil pageUtil);
    Admin get(String admin_name, Integer admin_id);
    Admin login(String admin_name);
    Integer getTotal(String admin_name);
}

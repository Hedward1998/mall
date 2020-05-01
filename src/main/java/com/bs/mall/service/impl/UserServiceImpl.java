package com.bs.mall.service.impl;

import com.bs.mall.dao.UserMapper;
import com.bs.mall.entity.User;
import com.bs.mall.service.UserService;
import com.bs.mall.util.OrderUtil;
import com.bs.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
//    private UserMapper userMapper;
//    @Resource(name = "userMapper")
//    public void setUserMapper(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
    
    @Autowired
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(User user) {
        return userMapper.insertOne(user)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(User user) {
        return userMapper.updateOne(user)>0;
    }
    
    @Override
    public boolean resetPassword(String user_name, String user_password) {
        return userMapper.resetPassword(user_name, user_password)>0;
    }

    @Override
    public List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil) {
        return userMapper.select(user,orderUtil,pageUtil);
    }

    @Override
    public User get(Integer user_id) {
        return userMapper.selectOne(user_id);
    }

    @Override
    public User login(User user) {
        return userMapper.selectByLogin(user);
    }

    @Override
    public Integer getTotal(User user) {
        return userMapper.selectTotal(user);
    }
}

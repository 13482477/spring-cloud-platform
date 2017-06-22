package com.siebre.uaa.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.uaa.user.entity.User;
import com.siebre.uaa.user.mapper.UserMapper;

/**
 * Created by AdamTang on 2017/3/6.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User loadUserByUsername(String username) {
        return this.userMapper.loadUserByUsername(username);
    }

    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    public List<User> listUser(PageInfo pageInfo) {
        return userMapper.selectByPage(pageInfo);
    }

    public User loadUserByID(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }


    @Transactional
    public void createUser(User user) {
        userMapper.insertSelective(user);
    }

    public void updateUser(User user) {
    	this.userMapper.updateByPrimaryKeySelective(user);
    }

    public boolean validateRepeatUsername(String username) {
        int count = userMapper.selectCountByUsername(username);
        return !(count > 0);
    }

    @Transactional
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        userMapper.deleteUserRoles(userId);
        if(roleIds!=null) {
            userMapper.insertUserRoles(userId, roleIds);
        }
    }
}

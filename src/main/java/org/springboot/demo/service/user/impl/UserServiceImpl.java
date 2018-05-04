package org.springboot.demo.service.user.impl;

import org.springboot.demo.dao.mapper.UserMapper;
import org.springboot.demo.module.User;
import org.springboot.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User selectUserByEmail(String email) {
        return null;
    }
}

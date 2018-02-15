package org.springboot.demo.service.user;

import org.springboot.demo.module.User;


public interface UserService {

    public User selectUserByEmail(String email);

}

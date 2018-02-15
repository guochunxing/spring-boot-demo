package org.springboot.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

        @PostMapping("/login")
        public Object login(@RequestParam("username") String username, @RequestParam("password") String password) {
            System.out.println("登录");
            System.out.println(username);
            System.out.println(password);
            return "700";
    }
}

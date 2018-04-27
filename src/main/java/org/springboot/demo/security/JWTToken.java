package org.springboot.demo.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class JWTToken extends UsernamePasswordToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package org.springboot.demo.security.filter;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springboot.demo.common.cost.JWTCost;
import org.springboot.demo.module.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User user = JSON.parseObject(req.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getToken(),
                            new ArrayList<>())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<String> arr = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        authorities.forEach(author -> arr.add(author.getAuthority()));
        String roles = String.join(",", arr);
        String token = Jwts.builder()
                .setSubject(roles)
                .setId(user.getId())
                .setIssuer(user.getEmail())
                .setExpiration(DateTime.now().plusMinutes(30).toDate())
                .signWith(SignatureAlgorithm.HS256, JWTCost.signatureKey)
                .compact();
        res.addHeader("Authorization", "Bearer " + token);
    }
}
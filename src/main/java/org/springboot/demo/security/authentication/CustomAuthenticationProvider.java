package org.springboot.demo.security.authentication;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.springboot.demo.dao.ext.ExtPermissionMapper;
import org.springboot.demo.dao.ext.ExtRoleMapper;
import org.springboot.demo.dao.mapper.UserMapper;
import org.springboot.demo.module.User;
import org.springboot.demo.module.UserExample;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ExtRoleMapper extRoleMapper;
    @Resource
    private ExtPermissionMapper extPermissionMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = authenticate(email, password);

        List<SimpleGrantedAuthority> authorize = authorize(user.getId());
        return new UsernamePasswordAuthenticationToken(user.getId(), null, authorize);
    }

    private User authenticate(String email, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isNotEmpty(users)) {
            User user = users.get(0);
            if (user == null) {
                throw new UsernameNotFoundException("user not exist");
            }
            if (bCryptPasswordEncoder.matches(password, user.getToken())) {
                return user;
            } else {
                throw new BadCredentialsException("bad credentials");
            }
        } else {
            throw new UsernameNotFoundException("user not exist");
        }
    }

    public List<SimpleGrantedAuthority> authorize(String userId) {
        String authorizeKey = "authorize:" + userId;
        String authority = stringRedisTemplate.opsForValue().get(authorizeKey);
        if (authority != null) {
            return JSON.parseArray(authority, SimpleGrantedAuthority.class);
        } else {
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            List<String> roles = extRoleMapper.selectRoleByUserId(userId);
            roles.forEach(role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            });
            List<String> permissions = extPermissionMapper.selectPermissionByUserId(userId);
            permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
            stringRedisTemplate.opsForValue().set(authorizeKey, JSON.toJSONString(grantedAuthorities));
            return grantedAuthorities;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

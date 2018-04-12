package org.springboot.demo.security.authentication;

import org.apache.commons.collections4.CollectionUtils;
import org.springboot.demo.dao.ext.ExtPermissionMapper;
import org.springboot.demo.dao.ext.ExtRoleMapper;
import org.springboot.demo.dao.mapper.UserMapper;
import org.springboot.demo.module.User;
import org.springboot.demo.module.UserExample;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isNotEmpty(users)) {
            User user = users.get(0);
            if (user == null) {
                throw new UsernameNotFoundException("user not exist");
            }
            if (bCryptPasswordEncoder.matches(password, user.getToken())) {
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                List<String> roles = extRoleMapper.selectRoleByUserId(user.getId());
                roles.forEach(role -> {
                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                });
                List<String> permissions = extPermissionMapper.selectPermissionByUserId(user.getId());
                permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
                return new UsernamePasswordAuthenticationToken(user, user.getEmail(), grantedAuthorities);
            } else {
                throw new BadCredentialsException("bad credentials");
            }
        } else {
            throw new UsernameNotFoundException("user not exist");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

package org.springboot.demo.security;

import org.apache.commons.collections4.CollectionUtils;
import org.springboot.demo.dao.ext.ExtPermissionMapper;
import org.springboot.demo.dao.mapper.UserMapper;
import org.springboot.demo.module.User;
import org.springboot.demo.module.UserExample;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component
public class CustomUserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ExtPermissionMapper extPermissionMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isNotEmpty(users)) {
            User user = users.get(0);
            if (user == null) {
                throw new UsernameNotFoundException("user not exist");
            }
            return new CustomUser(user.getName(), user.getToken(), getAuthorities(user.getId()));
        }
        //获取权限
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String userId) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<String> permissions = extPermissionMapper.selectPermissionByUserId(userId);
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorities;
    }
}

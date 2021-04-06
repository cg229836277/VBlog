package org.sang.config.impl;

import org.sang.dataobject.RoleDO;
import org.sang.dataobject.UserDO;
import org.sang.mapper.RoleMapper;
import org.sang.mapper.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userMapper.selectByUsername(username);
        if (userDO == null) {
            throw new UsernameNotFoundException(username);
        }
        RoleDO roleDO = roleMapper.selectByUserId(userDO.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Spring Security 角色名称默认使用 "ROLE_" 开头
        // authorities.add 可以增加多个用户角色，对于一个用户有多种角色的系统来说，
        // 可以通过增加用户角色表、用户--角色映射表，存储多个用户角色信息
        authorities.add(new SimpleGrantedAuthority("ROLE_" + (roleDO == null ? RoleDO.ROLE_NORMAL : roleDO.getName())));
        // 给 Spring Security 传入用户名、用户密码、用户角色。
        return new User(userDO.getUsername(), userDO.getPassword(), authorities);
    }
}
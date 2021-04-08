package org.sang.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.StringUtils;
import org.sang.config.utils.TokenUtils;
import org.sang.dataobject.RoleDO;
import org.sang.dataobject.UserDO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 存储 Token 的 Headers Key与 Value，默认是 Authorization
        final String tokenKey = "token";
        String token;
        try {
            token = request.getHeader(tokenKey);
        } catch (Exception e) {
            token = null;
        }
        if (!StringUtils.isEmpty(token)) {
            UserDO userDO = tokenUtils.validationToken(token);
            if (userDO != null) {
                // Spring Security 角色名称默认使用 "ROLE_" 开头
                // authorities.add 可以增加多个用户角色，对于一个用户有多种角色的系统来说，
                // 可以通过增加用户角色表、用户--角色映射表，存储多个用户角色信息
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                String roleName = userDO.getRoleName();
                if (roleName != null && roleName.length() > 0) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleDO.ROLE_NORMAL));
                }
                // 传入用户名、用户密码、用户角色。 这里的密码随便写的，用不上
                UserDetails userDetails = new User(userDO.getUsername(), userDO.getPassword(), authorities);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authentication.setDetails(userDetails.getUsername());
                // 授权
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}

package org.sang.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.TokenUtils;
import org.sang.dataobject.RoleDO;
import org.sang.dataobject.UserDO;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.mapper.RoleMapper;
import org.sang.mapper.UserMapper;
import org.sang.vo.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private RoleMapper roleMapper;

    private UserMapper userMapper;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RoleMapper roleMapper, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            String requestUrl = req.getRequestURL().toString();
            log.info("TokenLogin requestUrl:" + requestUrl);
            String method = req.getMethod();
            log.info("TokenLogin method:" + method);
            InputStream inputStream = req.getInputStream();
            //获取body数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String postContent = sb.toString();
            log.info("postContent:" + postContent);

            UserDO userDO = new ObjectMapper().readValue(postContent, UserDO.class);
            UserDO savedUserDO = userMapper.selectByUsername(userDO.getUsername());
            RoleDO roleDO = roleMapper.selectByUserId(savedUserDO.getId());
            String roleName = roleDO.getName();

            List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
            if (roleName != null && roleName.length() > 0) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleDO.ROLE_NORMAL));
            }
            log.info("attemptAuthentication ps:" + savedUserDO.getPassword());
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(savedUserDO.getUsername(), userDO.getPassword(), authorities));
        } catch (Exception e) {
            throw new ServiceException(ServiceExceptionEnum.AUTHORIZATION_FAIL_ERROR);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        User user = (User) auth.getPrincipal();
        UserDO savedUserDO = userMapper.selectByUsername(user.getUsername());
        RoleDO roleDO = roleMapper.selectByUserId(savedUserDO.getId());
        String roleName = roleDO.getName();
        String token = TokenUtils.createToken(savedUserDO, roleName);
        log.info("token is:" + token);
        savedUserDO.setToken(token);
        savedUserDO.setPassword("");

        CommonResult<UserDO> result = CommonResult.success(savedUserDO);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getWriter(), result);
        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        res.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        throw new ServiceException(ServiceExceptionEnum.AUTHORIZATION_FAIL_ERROR);
    }
}

package org.sang.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.StringUtils;
import org.sang.config.utils.TokenUtils;
import org.sang.dataobject.RoleDO;
import org.sang.dataobject.UserDO;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    public TokenAuthenticationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader("token");

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        try {
            // token置于header里
            String token = request.getHeader("token");
            log.info("header token is:" + token);
            if (token != null && !"".equals(token.trim())) {
                // parse the token.
                UserDO userDO = TokenUtils.validationToken(token);
                String userName = userDO.getUsername();
                String roleName = userDO.getRoleName();
                List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>(1);
                if (roleName != null && roleName.length() > 0) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleDO.ROLE_NORMAL));
                }
                if (!StringUtils.isEmpty(userName)) {
                    return new UsernamePasswordAuthenticationToken(userName, token, authorities);
                }
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException(ServiceExceptionEnum.AUTHORIZATION_FAIL_ERROR);
        }
        return null;
    }
}

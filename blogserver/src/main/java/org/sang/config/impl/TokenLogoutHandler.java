package org.sang.config.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.sang.exception.ServiceException;
import org.sang.vo.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.sang.exception.ServiceExceptionEnum.LOGOUT_FAILED;

@Slf4j
public class TokenLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            log.info("token is valid");
            token = null;
        }
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            CommonResult commonResult = CommonResult.success();
            mapper.writeValue(response.getWriter(), commonResult);
        } catch (Exception e) {
            throw new ServiceException(LOGOUT_FAILED);
        }
    }

}
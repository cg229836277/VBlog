package org.sang.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.vo.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 5200068540912465653L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 设置 Json 格式返回
        response.setContentType("application/json;charset=UTF-8");
        // 设置 HTTP 状态码为 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // PrintWriter 输出 Response 返回信息
        PrintWriter writer = response.getWriter();
        CommonResult errorResult = CommonResult.error(ServiceExceptionEnum.AUTHORIZATION_FAIL_ERROR.getCode(), ServiceExceptionEnum.AUTHORIZATION_FAIL_ERROR.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        // 将对象输出为 JSON 格式。可以通过重写 MyResponse 的 toString() ，直接通过 myResponse.toString() 即可
        writer.write(mapper.writeValueAsString(errorResult));
        authException.printStackTrace();
    }
}


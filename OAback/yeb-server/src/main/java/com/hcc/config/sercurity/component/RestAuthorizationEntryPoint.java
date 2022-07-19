package com.hcc.config.sercurity.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcc.pojo.RespBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录或者token失效时的返回结果
 */

@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        RespBean R = RespBean.error("未登录或重新登录");

        R.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(R));
        out.flush();  // 强推到浏览器
        out.close();


    }
}

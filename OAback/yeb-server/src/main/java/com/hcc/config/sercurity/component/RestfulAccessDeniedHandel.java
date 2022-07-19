package com.hcc.config.sercurity.component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcc.pojo.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当访问接口没有权限的时候，自定义返回结果
 */
@Component
public class RestfulAccessDeniedHandel implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        RespBean R = RespBean.error("权限不足( ╯□╰ )");
        R.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(R));
        out.flush();  // 强推到浏览器
        out.close();

    }
}

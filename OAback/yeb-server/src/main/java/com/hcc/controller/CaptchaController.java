package com.hcc.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码",produces="image/jpeg")
    @GetMapping("/captcha")
    public void capacha(HttpServletRequest request, HttpServletResponse response){
        //设置响应类型
        response.setDateHeader("Expires",0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //验证验证码文本
        String text = defaultKaptcha.createText();
        System.out.println("验证码"+text);

        //放验证码到sessio
        request.getSession().setAttribute("captcha",text);

        // 根据文本验证码内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream servletOutputStream = null;


        try {
            servletOutputStream=response.getOutputStream();
            ImageIO.write(image, "jpg", servletOutputStream);
            // 发送到浏览器
            servletOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

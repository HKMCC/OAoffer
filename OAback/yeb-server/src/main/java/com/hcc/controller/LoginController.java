package com.hcc.controller;


import com.hcc.pojo.Admin;
import com.hcc.pojo.AdminLoginParam;
import com.hcc.pojo.RespBean;
import com.hcc.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


/**
 * 登录
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后放回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){

        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);


    }


    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        //前端处理
        return RespBean.success("注销成功！");
    }


    @ApiOperation(value = "获取登录信息")
    @PostMapping("/admin/info")
    public Admin getAdmininof(Principal principal){
        //principal全局？？？类似于UserDetails??当前登录对象？？？
        if (principal==null){
            return null;

        }

        String username = principal.getName();
        Admin admin=adminService.getAdminbyUserName(username);
        admin.setPassword(null);
        //获取用户角色列表
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;

    }


}

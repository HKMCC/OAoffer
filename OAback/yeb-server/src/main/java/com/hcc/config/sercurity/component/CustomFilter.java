package com.hcc.config.sercurity.component;

import com.hcc.pojo.Menu;
import com.hcc.pojo.Role;
import com.hcc.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 权限控制
 * 根据请求url分析请求所需要的角色
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {


    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        //获取请求url
        String requestUrl=((FilterInvocation) object).getRequestUrl();
//        System.out.println("当前请求的url+"+requestUrl);
        //根据角色查询所有菜单
        List<Menu> menus = menuService.getMenusWithRole();
//        System.out.println("ssssssss"+menus);
        for (Menu menu:menus) {
            //判断请求url是否与菜单角色匹配
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
            //没匹配的默认登录可访问

        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}

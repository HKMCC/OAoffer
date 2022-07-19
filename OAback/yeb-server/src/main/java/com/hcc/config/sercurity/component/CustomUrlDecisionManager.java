package com.hcc.config.sercurity.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *
 * 权限控制
 * 判断用户角色
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            //当前url所需要角色
            String needRole = configAttribute.getAttribute();
//            System.out.println("当前前url所需要角色"+needRole);
            //判断是不是登录即可访问的角色在CustomFilter设置的
            if ("ROLE_LOGIN".equals(needRole)){
                //没登录
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录");
                }else {
                    return;
                }
            }
            //判断用户角色是否为url所需角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//获取当前用户角色？
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)){
//                    System.out.println("判断用户角色是否为url所需角色"+authority.getAuthority());
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足( ╯□╰ )");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}

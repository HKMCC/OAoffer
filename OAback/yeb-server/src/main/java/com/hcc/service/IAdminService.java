package com.hcc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.pojo.Admin;
import com.hcc.pojo.Menu;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */

public interface IAdminService extends IService<Admin> {
    /**
     * 登录之后返回Token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code ,HttpServletRequest request);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Admin getAdminbyUserName(String username);

    /**
     * 根据用户ID查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(String keywords);
    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    /**
     * 更新用户名密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    RespBean updateAdminPasswork(String oldPass, String pass, Integer adminId);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}

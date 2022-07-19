package com.hcc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

    List<Menu> getAllMenu();
}

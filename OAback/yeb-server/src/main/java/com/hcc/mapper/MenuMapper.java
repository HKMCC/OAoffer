package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface MenuMapper extends BaseMapper<Menu> {


    List<Menu> getMenusByAdminId(Integer id);

    /**
     * 根据角色获取用户列表
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> getAllMenu();
}

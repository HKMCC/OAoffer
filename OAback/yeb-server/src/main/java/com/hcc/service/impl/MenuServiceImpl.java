package com.hcc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.mapper.MenuMapper;
import com.hcc.pojo.Admin;
import com.hcc.pojo.Menu;
import com.hcc.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Menu> getMenusByAdminId() {

        // 获取 Security全局内的用户信息
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 首先先在redis中获取
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 根据用户id查菜单数据
        List<Menu> menuList = (List<Menu>) valueOperations.get("menu_"+admin.getId());
        // 判断List是否为空，即是判断redis中是否存在数据
        if (CollectionUtils.isEmpty(menuList)) {
            // 如果空的话，在数据库中查找
            menuList = menuMapper.getMenusByAdminId(admin.getId());
            // 设置到redis中
            valueOperations.set("menu_"+admin.getId(), menuList);
        }
        // 通过id查询数据库
        return menuList;

//        return menuMapper.getMenusByAdminId(((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 根据角色获取菜单列表
     * @return
     */

    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    /**
     * 获取所有菜单
     * @return
     */
    @Override
    public List<Menu> getAllMenu() {
        return menuMapper.getAllMenu();
    }
}

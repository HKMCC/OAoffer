package com.hcc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hcc.pojo.Menu;
import com.hcc.pojo.MenuRole;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.Role;
import com.hcc.service.IMenuRoleService;
import com.hcc.service.IMenuService;
import com.hcc.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/roles")
    public RespBean addRoles(@RequestBody Role role) {
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }if (roleService.save(role)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }


    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRoles(@PathVariable Integer rid) {
        if (roleService.removeById(rid)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/menu")
    public List<Menu> getMenu() {

        return menuService.getAllMenu();
    }

    @ApiOperation(value = "根据角色id查询菜单ID")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid) {
        return menuRoleService.list
                (new QueryWrapper<MenuRole>().eq("rid",rid))
                .stream()
                .map(MenuRole::getMid)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid,Integer[] mids) {
        return menuRoleService.updateMenuRole(rid,mids);
    }



}

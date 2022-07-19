package com.hcc.controller;


import com.hcc.pojo.Admin;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.Role;
import com.hcc.service.IAdminRoleService;
import com.hcc.service.IAdminService;
import com.hcc.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
@RestController

@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmins(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return RespBean.success("更新成功");
        }
       return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmins(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/roles")
    public RespBean updeteAdminRoles(Integer adminId,Integer[] rids){
        return adminService.updateAdminRole(adminId,rids);
    }
}

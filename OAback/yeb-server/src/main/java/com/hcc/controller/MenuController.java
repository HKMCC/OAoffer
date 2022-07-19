package com.hcc.controller;


import com.hcc.pojo.Menu;
import com.hcc.service.IAdminService;
import com.hcc.service.IMenuService;
import com.hcc.service.impl.MenuServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value ="通过用户i查询列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){

        return menuService.getMenusByAdminId();
    }


}

package com.hcc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.pojo.MenuRole;
import com.hcc.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenuRole(Integer rid, Integer[] mids);
}

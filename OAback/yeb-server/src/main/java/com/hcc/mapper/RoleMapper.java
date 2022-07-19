package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}

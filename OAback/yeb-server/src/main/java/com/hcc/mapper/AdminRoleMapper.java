package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.AdminRole;
import com.hcc.pojo.RespBean;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer updateAdminRole( @Param("adminId") Integer adminId, @Param("rids")Integer[] rids);
}

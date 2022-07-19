package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    /**
     * 更新角色菜单
     *
     * @param rid
     * @param mids
     * @return
     */
    Integer insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}

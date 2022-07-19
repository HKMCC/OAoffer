package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.Admin;
import com.hcc.pojo.Menu;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */

public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有操作员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(Integer id, String keywords);
}

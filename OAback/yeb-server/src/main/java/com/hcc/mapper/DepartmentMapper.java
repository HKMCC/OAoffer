package com.hcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.pojo.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(Integer parentId);

    /**
     * 添加部门
     *
     * @param dep
     */
    void addDep(Department dep);


    /**
     * 删除部门
     * @param dep
     */
    void deleteDep(Department dep);

}

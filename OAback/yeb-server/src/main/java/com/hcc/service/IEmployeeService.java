package com.hcc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.pojo.Employee;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * 查询所有员工列表
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkID();

    /**
     * 获取员工
     * @param employee
     * @return
     */
    RespBean addEmp(Employee employee);

    /**
     * 查询员工/导出员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有员工工资账套
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmoloyeeWithSalary(Integer currentPage, Integer size);
}

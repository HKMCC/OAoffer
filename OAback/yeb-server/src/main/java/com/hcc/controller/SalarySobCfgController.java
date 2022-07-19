package com.hcc.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hcc.pojo.Employee;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.RespPageBean;
import com.hcc.pojo.Salary;
import com.hcc.service.IEmployeeService;
import com.hcc.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *获取所有员工账套
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {
    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation("获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation("获取所有员工工资账套")
    @GetMapping("/")
    public RespPageBean getEmoloyeeWithSalary(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size
    ){


        return employeeService.getEmoloyeeWithSalary(currentPage,size);
    }

    @ApiOperation("更新员工账套")
    @PutMapping("/")
    public RespBean updateEmoloyeeWithSalary(Integer eid,Integer sid){
        if (employeeService.update(new UpdateWrapper<Employee>()
                .set("salaryId",sid).eq("id",eid))){
            return RespBean.success("更新成功！");
        }

        return RespBean.error("更新失败");
    }


}

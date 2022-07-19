package com.hcc.controller;


import com.hcc.pojo.RespBean;
import com.hcc.pojo.Salary;
import com.hcc.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    private ISalaryService salaryService;

    @ApiOperation("获取所有工资账套")
    @GetMapping("/")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation("添加工资账套")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)){
            return RespBean.success("添加工资账套成功");
        }

        return RespBean.error("添加工资账套失败");
    }

    @ApiOperation("删除工资账套")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id){

        if (salaryService.removeById(id)){
            return RespBean.success("删除工资账套成功！");
        }

        return RespBean.error("删除工资账套失败！");
    }

    @ApiOperation("更新工资账套")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){

        if (salaryService.updateById(salary)){
            return RespBean.success("更新工资账套成功！");
        }

        return RespBean.error("更新工资账套失败！");
    }
}

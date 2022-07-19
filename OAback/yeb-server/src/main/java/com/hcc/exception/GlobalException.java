//package com.hcc.exception;
//
//import com.hcc.pojo.RespBean;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.sql.SQLException;
//import java.sql.SQLIntegrityConstraintViolationException;
//
//@RestControllerAdvice
//public class GlobalException {
//
//    @ExceptionHandler(SQLException.class)
//    public RespBean mySqlException(SQLException e){
//        if (e instanceof SQLIntegrityConstraintViolationException){
//            return RespBean.error("我求求你别乱搞了~~~");
//        }
//        return RespBean.error("数据库异常，操作失败");
//    }
//
//}

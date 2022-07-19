package com.hcc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {
    //总条数
    private Long total;
    //数据List
    private List<?> data;

}

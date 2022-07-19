package com.hcc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object result;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message) {

        return new RespBean(200, message,null);
    }

    /**
     * 成功返回 带结果
     * @param message
     * @param result
     * @return
     */
    public static RespBean success(String message, Object result) {
        return new RespBean(200, message, result);
    }

    /**
     * 失败返回
     * @param message
     * @return
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }
    /**
     * 失败返回带结果
     * @param message
     * @param result
     * @return
     */
    public static RespBean error(String message, Object result) {
        return new RespBean(500, message, result);
    }



}

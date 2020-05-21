package com.admin.result;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 标准通用的请求返回对象
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 4649183859410416954L;

    /**
     * 处理结果编号，0为成功，负数为错误，正数为成功但有提示
     */
    private Integer code;
    /**
     * 提示或错误描述，可为空
     */
    private String msg;
    /**
     * 响应时间，可为空
     */
    private Date time;
    /**
     * 返回的数据
     */
    private T data;

    public Result() {
        //this.time = new Date();
    }

    @SuppressWarnings("unchecked")
    public static <T> Result<T> getInstance() {
        return new Result<>();
    }

    /**
     * 成功
     *
     * @return 返回的对象
     */
    public Result<T> ok() {
        this.code = 0;
        return this;
    }

    /**
     * 错误信息
     *
     * @param msg 错误描述
     * @return 返回的对象
     */
    public Result<T> error(String msg) {
        this.code = -1;
        this.msg = msg;
        return this;
    }

    /**
     * 错误信息
     *
     * @param msg 错误描述
     * @return 返回的对象
     */
    public Result<T> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    /**
     * 系统错误
     *
     * @return 返回的对象
     */
    public Result<T> systemFailure() {
        this.code = -1;
        this.msg = "系统错误";
        return this;
    }

    /**
     * 登录状态已过期
     *
     * @return 返回的对象
     */
    public Result<T> tokenHasExpired() {
        this.code = -100;
        this.msg = "登录状态已过期";
        return this;
    }

    /**
     * 请求过于频繁时返回的对象
     *
     * @return 返回的对象
     */
    public Result<T> requestRejection() {
        this.code = -98;
        this.msg = "请求过于频繁";
        return this;
    }

    /**
     * 缺少此功能访问权限
     *
     * @return 返回的对象
     */

    public Result<T> permissionLacking() {
        this.code = -97;
        this.msg = "缺少此功能访问权限";
        return this;
    }

    /**
     * 缺少此功能访问权限
     *
     * @return 返回的对象
     */
    public Result<T> ticketExpired() {
        this.code = -96;
        this.msg = "票据状态已过期";
        return this;
    }

    /**
     * 缺少此功能访问权限
     *
     * @return 返回的对象
     */
    public Result<T> invalidRequest() {
        this.code = -1;
        this.msg = "无效的请求";
        return this;
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> setTime(Date time) {
        this.time = time;
        return this;
    }


    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}

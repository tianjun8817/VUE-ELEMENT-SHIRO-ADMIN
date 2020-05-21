package com.admin.mapper.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * oper_log
 * @author 
 */
@Data
@TableName("oper_log")
public class OperLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 操作日志
     */
    private String msg;

    private Long uid;

    private String uname;

    private Date ctime;

    private String res;

    private String clazz;

    private Long refId;

    private String type;
}
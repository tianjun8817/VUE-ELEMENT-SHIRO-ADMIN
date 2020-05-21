package com.admin.mapper.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限信息表
 *
 * @program: common-admin-console
 * @author: Xuntj
 * @create: 2020-04-07 13:44
 **/
@Data
@TableName("sys_perm")
public class SysPerm implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;
    /**
     * 权限编号
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 权限说明
     */
    private String perm;
    /**
     * 权限说明
     */
    private String memo;
    /**
     * 状态
     *
     * @see com.admin.constants.GenericStatus
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 修改时间
     */
    private Date utime;
    /**
     * 创建人ID
     */
    private Long cid;
    /**
     * 创建人名称
     */
    private String cname;
    /**
     * 是否选中
     */
    @TableField(exist = false)
    private boolean checked;
}
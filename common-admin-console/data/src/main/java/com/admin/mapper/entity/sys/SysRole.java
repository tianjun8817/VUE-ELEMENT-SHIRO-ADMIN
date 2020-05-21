/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.entity.sys;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 备注
     */
    private String roleCode;
    /**
     * 权限值
     */
    @TableField(exist = false)
    private List<String> perms;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 创建时间
     */
    private Date utime;
    /**
     * 状态
     *
     * @see com.admin.constants.GenericStatus
     */
    private Integer state;
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

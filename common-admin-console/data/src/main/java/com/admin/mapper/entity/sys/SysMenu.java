/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long pid;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单code
     */
    private String code;
    /**
     * 菜单URL
     */
    @TableField(exist = false)
    private List<SysMenu> children;
    /**
     * 菜单URL
     */
    @TableField(exist = false)
    private boolean checked;
}

package com.admin.mapper.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("sys_role_perm")
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePerm implements Serializable {
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
     * 角色
     */
    private Long rid;
    /**
     * 权限编号
     */
    private Long permId;
}
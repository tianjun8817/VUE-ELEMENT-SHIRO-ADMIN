/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysRolePerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 角色-权限值关联表
 */
@Mapper
@Component(value = "sysRolePermMapper")
public interface SysRolePermMapper extends BaseMapper<SysRolePerm> {
}

/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 用户与角色对应关系
 */
@Mapper
@Component(value = "sysUserRoleMapper")
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}

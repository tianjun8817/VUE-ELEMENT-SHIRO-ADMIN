/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 角色管理
 */
@Mapper
@Component(value = "sysRoleMapper")
public interface SysRoleMapper extends BaseMapper<SysRole> {


}

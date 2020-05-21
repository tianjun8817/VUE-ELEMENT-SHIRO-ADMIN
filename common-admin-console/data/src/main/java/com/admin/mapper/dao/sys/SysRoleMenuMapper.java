/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色与菜单对应关系
 */
@Mapper
@Component(value = "sysRoleMenuMapper")
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

}

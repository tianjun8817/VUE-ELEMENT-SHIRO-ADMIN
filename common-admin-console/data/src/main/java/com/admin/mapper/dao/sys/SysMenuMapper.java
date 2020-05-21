/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */
package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 菜单管理
 */
@Mapper
@Component(value = "sysMenuMapper")
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}

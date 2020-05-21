/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysPerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 权限值管理
 */
@Mapper
@Component(value = "sysPermMapper")
public interface SysPermMapper extends BaseMapper<SysPerm> {

}

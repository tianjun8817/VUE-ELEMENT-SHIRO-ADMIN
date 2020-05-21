/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.mapper.dao.sys;

import com.admin.mapper.entity.sys.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统用户
 */
@Mapper
@Component(value = "sysUserMapper")
public interface SysUserMapper extends BaseMapper<SysUser> {

}

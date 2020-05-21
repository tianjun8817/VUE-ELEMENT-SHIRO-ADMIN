/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */
package com.admin.mapper.dao.log;

import com.admin.mapper.entity.log.OperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {
}
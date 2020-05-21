/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.log;


import com.admin.mapper.entity.log.OperLog;
import com.admin.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @description: 操作日志信息服务实现类
 * @program: common-admin-console
 * @author: tianjun87001@163.com
 * @create: 2020-04-12 21:47
 **/
public interface OperateLogService extends IService<OperLog> {
    /**
     * 分页查询操作日志数据
     *
     * @param refId
     * @param curPage
     * @param limit
     * @return
     */
    Result<OperLog> queryPage(Long refId, long curPage, long limit);
}

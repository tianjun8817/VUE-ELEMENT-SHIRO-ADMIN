/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */
package com.admin.service.log.impl;

import com.admin.mapper.dao.log.OperLogMapper;
import com.admin.mapper.entity.log.OperLog;
import com.admin.result.Result;
import com.admin.service.log.OperateLogService;
import com.admin.utils.PageUtils;
import com.admin.utils.Query;
import com.admin.utils.CommonStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @description: 操作日志信息服务实现类
 * @program: common-admin-console
 * @author: tianjun87001@163.com
 * @create: 2020-04-12 21:47
 **/
@Service("operateLogService")
public class OperateLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperateLogService {

    /**
     * @param refId
     * @param curPage
     * @param limit
     * @return
     */
    @Override
    public Result queryPage(Long refId, long curPage, long limit) {
        //  分页查询
        IPage<OperLog> page = this.page(
                new Query<OperLog>().getPage(curPage, limit),
                new QueryWrapper<OperLog>()
                        .eq(CommonStringUtils.isNotBlank(refId), "ref_id",
                                CommonStringUtils.isNotBlank(refId) ? refId : CommonStringUtils.EMPTY)
                        .orderByDesc("id")
        );
        // 返回结果
        return Result.getInstance().ok().setData(new PageUtils(page));
    }
}

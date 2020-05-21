/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.log;


import com.admin.controller.base.AbstractBaseController;
import com.admin.result.Result;
import com.admin.service.log.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.admin.constants.Constant.PAGE_CURRENT;
import static com.admin.constants.Constant.PAGE_LIMIT;

/**
 * 操作日志相关
 */
@RestController
@RequestMapping("/api/log")
public class OperateLogController extends AbstractBaseController {
    /**
     * 操作日志相关服务类
     */
    @Autowired
    OperateLogService operateLogService;

    /**
     * 获取操作日志列表信息
     */
    @RequestMapping("/oper-infos")
    public Result infos(
            @RequestParam(required = false) Long refId,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return operateLogService.queryPage(refId, curPage, limit);
    }

}

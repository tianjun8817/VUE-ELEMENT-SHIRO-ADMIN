/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.sys;


import com.admin.controller.base.AbstractBaseController;
import com.admin.mapper.entity.sys.SysPerm;
import com.admin.result.Result;
import com.admin.service.sys.SysPermService;
import com.admin.utils.CommonStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.admin.constants.Constant.PAGE_CURRENT;
import static com.admin.constants.Constant.PAGE_LIMIT;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/api/sys/perm")
public class PermInfoController extends AbstractBaseController {
    /**
     * 权限信息相关服务类
     */
    @Autowired
    SysPermService sysPermService;

    /**
     * 获取权限列表信息
     */
    @RequestMapping("/infos")
    public Result infos(
            @RequestParam(required = false) String perm,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysPermService.queryPage(perm, curPage, limit);
    }

    /**
     * 添加权限信息
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SysPerm perm) {
        return sysPermService.saveEntity(perm);
    }

    /**
     * 查看权限详情
     */
    @RequestMapping("/view")
    public Result view(@RequestParam(required = false) Long id) {
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(id)) {
            return Result.getInstance().invalidRequest();
        }
        // 返回查询结果
        return Result.getInstance().setData(sysPermService.getOne(new QueryWrapper<SysPerm>().eq("id", id))).ok();
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/role-infos")
    public Result infos(
            @RequestParam(required = false) Long permId,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysPermService.queryRolePage(permId, curPage, limit);
    }
}

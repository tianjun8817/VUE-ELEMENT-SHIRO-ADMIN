/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.sys;


import com.admin.controller.base.AbstractBaseController;
import com.admin.mapper.entity.sys.SysRole;
import com.admin.result.Result;
import com.admin.service.sys.SysRoleService;
import com.admin.utils.CommonStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.admin.constants.Constant.PAGE_CURRENT;
import static com.admin.constants.Constant.PAGE_LIMIT;

/**
 * 角色信息相关Controller
 */
@RestController
@RequestMapping("/api/sys/role")
public class RoleInfoController extends AbstractBaseController {

    @Autowired
    SysRoleService sysRoleService;

    /**
     * 获取权限列表信息
     */
    @RequestMapping("/infos")
    public Result infos(
            @RequestParam(required = false) String perm,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysRoleService.queryPage(perm, curPage, limit);
    }

    /**
     * 添加权限信息
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SysRole role) {
        return sysRoleService.saveEntity(role);
    }


    /**
     * 查看角色详情
     */
    @RequestMapping("/view")
    public Result view(@RequestParam(required = false) Long id) {
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(id)) {
            return Result.getInstance().invalidRequest();
        }
        // 返回查询结果
        return Result.getInstance().setData(sysRoleService.getOne(new QueryWrapper<SysRole>().eq("id", id))).ok();
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/perm-infos")
    public Result permInfos(
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "false") boolean full,
            @RequestParam(required = false) String perm,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysRoleService.queryRolePermPage(roleId, full, perm, curPage, limit);
    }

    /**
     * 保存角色对应的权限信息
     */
    @RequestMapping("/save-perms")
    public Result savePerms(
            @RequestParam Long roleId,
            @RequestParam(required = false) Set<Long> permIds) {
        return sysRoleService.saveRolePerms(roleId, permIds);
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/role-menus")
    public Result roleMenus(
            @RequestParam Long roleId,
            @RequestParam(defaultValue = "true") boolean full) {
        return sysRoleService.queryRoleMenuTree(full, roleId);
    }

    /**
     * 保存角色对应的权限信息
     */
    @RequestMapping("/save-menus")
    public Result saveMenus(
            @RequestParam Long roleId,
            @RequestParam(required = false) Set<Long> menuIds) {
        return sysRoleService.saveRoleMenus(roleId, menuIds);
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/user-infos")
    public Result permsInfos(
            @RequestParam Long roleId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String mobile,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysRoleService.queryRoleUser(roleId, userName, mobile, curPage, limit);
    }
}

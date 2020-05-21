/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.sys;


import com.admin.controller.base.AbstractBaseController;
import com.admin.dto.sys.LoginAuthDTO;
import com.admin.mapper.entity.sys.SysRole;
import com.admin.mapper.entity.sys.SysUser;
import com.admin.result.Result;
import com.admin.service.sys.SysMenuService;
import com.admin.service.sys.SysUserService;
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
 * 登录相关
 */
@RestController
@RequestMapping("/api/sys/user")
public class UserInfoController extends AbstractBaseController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public Result curInfo() {
        LoginAuthDTO dto = getUser();
        dto.setMenus(sysMenuService.getUserTreeList(dto.getSysUser().getId()).getData());
        return Result.getInstance().ok().setData(dto);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/infos")
    public Result infos(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysUserService.queryPage(username, curPage, limit);
    }


    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/view")
    public Result view(@RequestParam(required = false) Long id) {
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(id)) {
            return Result.getInstance().invalidRequest();
        }
        // 返回查询结果
        return Result.getInstance().setData(sysUserService.getOne(new QueryWrapper<SysUser>().eq("id", id))).ok();
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/role-infos")
    public Result roleInfos(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "false") boolean full,
            @RequestParam(required = false) String roleName,
            @RequestParam(defaultValue = PAGE_CURRENT) Long curPage,
            @RequestParam(defaultValue = PAGE_LIMIT) Long limit) {
        return sysUserService.queryRolePage(userId, full, roleName, curPage, limit);
    }

    /**
     * 获取权限对应的角色列表
     */
    @RequestMapping("/menu-tree")
    public Result menuTree(
            @RequestParam(required = false) Long userId) {
        return sysUserService.queryMenuTree(userId);
    }

    /**
     * 保存角色对应的权限信息
     */
    @RequestMapping("/save-roles")
    public Result saveRoles(
            @RequestParam Long userId,
            @RequestParam(required = false) Set<Long> roleIds) {
        return sysUserService.saveRoles(userId, roleIds);
    }

    /**
     * 添加权限信息
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SysUser user) {
        return sysUserService.saveEntity(user);
    }

    /**
     * 重置用户密码
     */
    @RequestMapping("/reset-password")
    public Result resetPassword(@RequestParam Long uid, @RequestParam(defaultValue = "") String password) {
        return sysUserService.resetPassword(uid, password);
    }


}

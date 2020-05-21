/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys;

import com.admin.mapper.entity.sys.SysRole;
import com.admin.mapper.entity.sys.SysUser;
import com.admin.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;


/**
 * 系统用户
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 分页查询用户数据
     *
     * @param username
     * @param curPage
     * @param limit
     * @return
     */
    Result queryPage(String username, long curPage, long limit);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据用户编号查询用户拥有的角色信息
     *
     * @param userId
     * @return
     */
    Result queryMenuTree(Long userId);

    /**
     * 设置用户对应的角色
     *
     * @param userId  用户ID
     * @param roleIds 角色Id集合
     * @return
     */
    Result saveRoles(Long userId, Set<Long> roleIds);

    /**
     * 用户对应的角色列表
     *
     * @param userId   用户ID
     * @param full     是否查询所有权限信息
     * @param roleName 角色名称
     * @return
     */
    Result queryRolePage(Long userId, boolean full, String roleName, long curPage, long limit);

    /**
     * 用户保存
     *
     * @param user
     * @return
     */
    Result saveEntity(SysUser user);

    /**
     * 密码修改
     *
     * @param uid
     * @param password
     * @return
     */
    Result resetPassword(Long uid, String password);
}

/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys;

import com.admin.mapper.entity.sys.SysRole;
import com.admin.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;


/**
 * 角色与菜单对应关系
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 分页查询角色数据
     *
     * @param roleCode
     * @param curPage
     * @param limit
     * @return
     */
    Result queryPage(String roleCode, long curPage, long limit);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List queryMenuIdList(Long... roleId);

    /**
     * 角色保存
     *
     * @param sysRole
     * @return
     */
    Result saveEntity(SysRole sysRole);

    /**
     * 角色对应的权限列表
     *
     * @param roleId 角色ID
     * @param full   是否查询所有权限信息
     * @param perm   权限值
     * @return
     */
    Result queryRolePermPage(Long roleId, boolean full, String perm, long curPage, long limit);

    /**
     * 设置角色对应的权限信息
     *
     * @param roleId  角色ID
     * @param permIds 权限ID集合
     * @return
     */
    Result saveRolePerms(Long roleId, Set<Long> permIds);

    /**
     * 角色对应的菜单列表
     *
     * @param roleId 角色ID
     * @param full   是否查询所有菜单信息
     * @return
     */
    Result queryRoleMenuTree(boolean full, Long... roleId);

    /**
     * 设置角色对应的菜单信息
     *
     * @param roleId  角色ID
     * @param menuIds 权限ID集合
     * @return
     */
    Result saveRoleMenus(Long roleId, Set<Long> menuIds);

    /**
     * 设置角色对应的菜单信息
     *
     * @param roleId 角色ID
     * @return
     */
    Result queryRoleUser(Long roleId, String userName, String mobile,
                         long curPage, long limit);
}

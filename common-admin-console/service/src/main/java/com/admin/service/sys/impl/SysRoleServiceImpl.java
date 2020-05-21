/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys.impl;

import com.admin.aspect.annotation.LogAnnotation;
import com.admin.aspect.annotation.RefIdAnnotation;
import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.constants.GenericStatus;
import com.admin.mapper.dao.sys.*;
import com.admin.mapper.entity.sys.*;
import com.admin.result.Result;
import com.admin.service.sys.SysRoleService;
import com.admin.utils.MenuUtils;
import com.admin.utils.PageUtils;
import com.admin.utils.Query;
import com.admin.utils.CommonStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色与菜单对应关系
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    SysPermMapper sysPermMapper;
    @Autowired
    SysRolePermMapper sysRolePermMapper;
    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 分页查询角色数据
     *
     * @param roleCode
     * @param curPage
     * @param limit
     * @return
     */
    @Override
    public Result queryPage(String roleCode, long curPage, long limit) {
        //  分页查询
        IPage<SysRole> page = this.page(
                new Query<SysRole>().getPage(curPage, limit),
                new QueryWrapper<SysRole>()
                        .like(CommonStringUtils.isNotBlank(roleCode), "role_code",
                                CommonStringUtils.isNotBlank(roleCode) ? roleCode : CommonStringUtils.EMPTY)
                        .orderByDesc("id")
        );
        // 返回结果
        return Result.getInstance().ok().setData(new PageUtils(page));
    }

    /**
     * 根据角色ID，获取菜单ID列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List queryMenuIdList(Long... roleId) {
        return sysRoleMenuMapper.selectObjs(new QueryWrapper<SysRoleMenu>().select("mid").in("rid", roleId));
    }

    /**
     * 角色保存
     *
     * @param sysRole
     * @return
     */
    @Override
    @LogAnnotation(msg = "操作角色信息功能.")
    public Result saveEntity(SysRole sysRole) {
        sysRole.setUtime(new Date());
        // 判断权限值是否已经存在
        SysRole role = this.getOne(new QueryWrapper<SysRole>().select("id").eq("role_code", sysRole.getRoleCode()));
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(sysRole.getId())) {
            // 权限值已经存在
            if (role != null) {
                return Result.getInstance().error(-201, "角色已存在,请检查");
            }
            // 当前登录用户
            SysUser sysuser = ShiroUtils.getUser();
            sysRole.setId(IdWorker.getId());
            sysRole.setCtime(new Date());
            sysRole.setState(GenericStatus.ENABLED);
            sysRole.setCid(sysuser.getId());
            sysRole.setCname(sysuser.getUsername());
            this.save(sysRole);
        }
        // 修改操作
        else {
            if (!CommonStringUtils.equals(role.getId(), sysRole.getId())) {
                return Result.getInstance().error(-201, "角色已存在,请检查");
            }
            this.updateById(sysRole);
        }
        return Result.getInstance().ok().setData(sysRole);
    }

    /**
     * 角色对应的权限列表
     *
     * @param roleId 角色ID
     * @param full   是否查询所有权限信息
     * @return
     */
    @Override
    public Result queryRolePermPage(Long roleId, boolean full, String perm, long curPage, long limit) {
        IPage<SysPerm> page = null;
        // 查询所有权限,然后进行角色判断
        if (full) {
            //  分页查询
            page = sysPermMapper.selectPage(
                    new Query<SysPerm>().getPage(curPage, limit),
                    new QueryWrapper<SysPerm>()
                            .eq("state", GenericStatus.ENABLED)
                            .like(CommonStringUtils.isNotBlank(perm), "perm", CommonStringUtils.isNotBlank(perm) ? perm : "")
                            .orderByDesc("id")
            );
            if (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
                // 获取权限ID
                List<Long> permIds = page.getRecords().stream().map(SysPerm::getId).collect(Collectors.toList());
                // 设置已拥有标记
                List hasPermIds = sysRolePermMapper.selectObjs(new QueryWrapper<SysRolePerm>().select("perm_id")
                        .in("perm_id", permIds)
                        .eq("rid", roleId));
                if (CollectionUtils.isNotEmpty(hasPermIds)) {
                    // 设置拥有标记
                    page.getRecords().stream().forEach(permInfo -> {
                        if (hasPermIds.contains(permInfo.getId())) {
                            permInfo.setChecked(true);
                        }
                    });
                }
            }
        }
        // 查询角色所拥有的权限列表
        else {
            //  分页查询
            page = sysPermMapper.selectPage(
                    new Query<SysPerm>().getPage(curPage, limit),
                    new QueryWrapper<SysPerm>()
                            .eq("state", GenericStatus.ENABLED)
                            .like(CommonStringUtils.isNotBlank(perm), "perm", CommonStringUtils.isNotBlank(perm) ? perm : "")
                            .inSql("id", "select perm_id from sys_role_perm where rid = " + roleId)
                            .orderByDesc("id")
            );
        }
        return Result.getInstance().ok().setData(new PageUtils(page));
    }

    /**
     * 设置角色对应的权限信息
     *
     * @param roleId  角色ID
     * @param permIds 权限编号集合
     * @return
     */
    @Override
    @LogAnnotation(msg = "操作角色权限信息.", recordRet = false)
    public Result saveRolePerms(@RefIdAnnotation Long roleId, Set<Long> permIds) {
        // 首先清除所有权限
        sysRolePermMapper.delete(new QueryWrapper<SysRolePerm>().eq("rid", roleId));
        // 授予权限值
        if (CollectionUtils.isNotEmpty(permIds)) {
            // 添加角色信息
            permIds.stream().forEach(permId -> {
                sysRolePermMapper.insert(new SysRolePerm(IdWorker.getId(), roleId, permId));
            });
        }
        return Result.getInstance().ok();
    }

    /**
     * 角色对应的菜单列表
     *
     * @param roleId 角色ID
     * @param full   是否查询所有菜单信息
     * @return
     */
    @Override
    public Result queryRoleMenuTree(boolean full, Long... roleId) {
        if (full) {
            List<SysMenu> allMenus = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().select("id", "name", "pid", "code"));
            List<Long> roleMenuIds = this.queryMenuIdList(roleId);
            allMenus.forEach(menu -> {
                if (roleMenuIds.contains(menu.getId())) {
                    menu.setChecked(true);
                }
            });
            return Result.getInstance().ok().setData(MenuUtils.getMenuTreeList(allMenus));
        }
        // 只查询角色对应的菜单树
        else {
            List<SysMenu> menus = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().select("id", "name", "pid", "code")
                    .inSql("id", "select mid from sys_role_menu where rid in (" + CommonStringUtils.strip(Arrays.toString(roleId),"[]") + ")"));
            return Result.getInstance().ok().setData(MenuUtils.getMenuTreeList(menus));
        }
    }

    /**
     * 设置角色对应的菜单信息
     *
     * @param roleId  角色ID
     * @param menuIds 权限ID集合
     * @return
     */
    @Override
    public Result saveRoleMenus(Long roleId, Set<Long> menuIds) {
        // 首先清除所有权限
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("rid", roleId));
        // 授予权限值
        if (CollectionUtils.isNotEmpty(menuIds)) {
            // 添加角色信息
            menuIds.stream().forEach(menuId -> {
                sysRoleMenuMapper.insert(new SysRoleMenu(IdWorker.getId(), roleId, menuId));
            });
        }
        return Result.getInstance().ok();
    }

    /**
     * 设置角色对应的菜单信息
     *
     * @param roleId 角色ID
     * @return
     */
    @Override
    public Result queryRoleUser(Long roleId, String userName, String mobile,
                                long curPage, long limit) {
        //  分页查询
        IPage<SysUser> page = sysUserMapper.selectPage(
                new Query<SysUser>().getPage(curPage, limit),
                new QueryWrapper<SysUser>()
                        .eq("state", GenericStatus.ENABLED)
                        .like(CommonStringUtils.isNotBlank(userName), "username", CommonStringUtils.isNotBlank(userName) ? userName : "")
                        .like(CommonStringUtils.isNotBlank(mobile), "mobile", CommonStringUtils.isNotBlank(mobile) ? mobile : "")
                        .inSql("id", "select uid from sys_user_role where rid = " + roleId)
                        .orderByDesc("id")
        );
        return Result.getInstance().ok().setData(new PageUtils(page));
    }
}

/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys.impl;

import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.mapper.dao.sys.SysMenuMapper;
import com.admin.mapper.entity.sys.SysMenu;
import com.admin.redis.MenusRDSService;
import com.admin.result.Result;
import com.admin.service.sys.SysMenuService;
import com.admin.service.sys.SysRoleService;
import com.admin.service.sys.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.admin.constants.Constant.SUPER_ADMIN;


/**
 * 系统菜单信息服务类
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MenusRDSService menusRDSService;


    /**
     * 获取用户菜单列表
     *
     * @param userId
     * @return
     */
    @Override
    public Result getUserTreeList(Long userId) {
        // 判断菜单信息是否存在
        if (menusRDSService.exists(userId)) {
            return Result.getInstance().ok().setData(menusRDSService.get(String.valueOf(userId)));
        }
        // 判断用户角色信息
        List<String> roles = ShiroUtils.getLoginAuthDTO().getRoles();
        //系统管理员，拥有最高权限
        if (CollectionUtils.isNotEmpty(roles) && roles.contains(SUPER_ADMIN)) {
            // 菜单信息去重
            List<SysMenu> menus = this.list().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(SysMenu::getId))), ArrayList::new));
            // 菜单信息去重
            // 获取树形菜单
            menusRDSService.save(String.valueOf(userId), menus, 24 * 60 * 60);
            // 返回结果
            return Result.getInstance().ok().setData(menus);
        }
        // 角色相关信息
        List<Long> roleIds = sysUserService.queryRoleIdList(userId);
        // 用户不存在角色信息
        if (CollectionUtils.isEmpty(roleIds)) {
            return Result.getInstance().ok();
        }
        // 根据菜单相关信息
        List<SysMenu> menuList = new ArrayList<>();
        // 根据角色信息,查询出菜单信息
        roleIds.forEach(roleId -> {
            List menuIds = sysRoleService.queryMenuIdList(roleId);
            // 查询出菜单信息
            List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>().in("id", menuIds));
            menuList.addAll(menus);
        });
        // 判断菜单是否为空
        if (CollectionUtils.isEmpty(menuList)) {
            return Result.getInstance().ok();
        }
        // 菜单信息去重
        List<SysMenu> menus = menuList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(SysMenu::getId))),
                ArrayList::new));
        // 菜单信息存入redis中
        menusRDSService.save(String.valueOf(userId), menus, 24 * 60 * 60);
        return Result.getInstance().ok().setData(menus);
    }
}

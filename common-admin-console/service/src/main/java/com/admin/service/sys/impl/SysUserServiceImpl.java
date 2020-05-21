/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys.impl;


import com.admin.aspect.annotation.LogAnnotation;
import com.admin.aspect.annotation.RefIdAnnotation;
import com.admin.config.shiro.redis.RedisCacheManager;
import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.constants.GenericStatus;
import com.admin.mapper.dao.sys.SysRoleMapper;
import com.admin.mapper.dao.sys.SysUserMapper;
import com.admin.mapper.dao.sys.SysUserRoleMapper;
import com.admin.mapper.entity.sys.*;
import com.admin.result.Result;
import com.admin.service.sys.SysRoleService;
import com.admin.service.sys.SysUserService;
import com.admin.utils.CommonStringUtils;
import com.admin.utils.PageUtils;
import com.admin.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.admin.constants.Constant.DEFAULT_PASSWORD;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    AuthenticatingRealm adminUserRealm;

    /**
     * 分页查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public Result queryPage(String username, long curPage, long limit) {
        IPage<SysUser> page = this.page(
                new Query<SysUser>().getPage(curPage, limit),
                new QueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "username",
                                StringUtils.isNotBlank(username) ? username : StringUtils.EMPTY)
        );
        return Result.getInstance().ok().setData(new PageUtils(page));
    }

    /**
     * 根据用户ID，获取角色ID列表
     *
     * @param userId
     */
    @Override
    public List queryRoleIdList(Long userId) {
        return sysUserRoleMapper.selectObjs(new QueryWrapper<SysUserRole>().select("rid").eq("uid", userId));
    }

    /**
     * 根据用户编号查询用户拥有的菜单信息
     *
     * @param userId
     * @return
     */
    @Override
    public Result queryMenuTree(Long userId) {
        // 查询用户所拥有的角色编号
        List roleIds = sysUserRoleMapper.selectObjs(new QueryWrapper<SysUserRole>().select("rid")
                .eq("uid", userId));
        // 判断用户是否拥有角色信息
        if (CollectionUtils.isEmpty(roleIds)) {
            return Result.getInstance().ok();
        }
        return sysRoleService.queryRoleMenuTree(false, Arrays.stream(roleIds.toArray()).map(roleId -> Long.valueOf(roleId.toString())).toArray(Long[]::new));
    }

    /**
     * 设置用户对应的角色
     *
     * @param userId  用户ID
     * @param roleIds 角色Id集合
     * @return
     */
    @Override
    public Result saveRoles(Long userId, Set<Long> roleIds) {
        // 首先清除所有权限
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("uid", userId));
        // 授予权限值
        if (CollectionUtils.isNotEmpty(roleIds)) {
            // 添加角色信息
            roleIds.stream().forEach(roleId -> {
                sysUserRoleMapper.insert(new SysUserRole(IdWorker.getId(), userId, roleId));
            });
        }
        return Result.getInstance().ok();
    }

    /**
     * 用户对应的角色列表
     *
     * @param userId   用户ID
     * @param full     是否查询所有权限信息
     * @param roleName 角色名称
     * @param curPage
     * @param limit
     * @return
     */
    @Override
    public Result queryRolePage(Long userId, boolean full, String roleName, long curPage, long limit) {
        IPage<SysRole> page = null;
        // 查询所有权限,然后进行角色判断
        if (full) {
            //  分页查询
            page = sysRoleMapper.selectPage(
                    new Query<SysRole>().getPage(curPage, limit),
                    new QueryWrapper<SysRole>()
                            .eq("state", GenericStatus.ENABLED)
                            .like(CommonStringUtils.isNotBlank(roleName), "role_name", CommonStringUtils.isNotBlank(roleName) ? roleName : "")
                            .orderByDesc("id")
            );
            if (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
                // 获取权限ID
                List<Long> rids = page.getRecords().stream().map(SysRole::getId).collect(Collectors.toList());
                // 设置已拥有标记
                List hasRoleIds = sysUserRoleMapper.selectObjs(new QueryWrapper<SysUserRole>().select("rid")
                        .in("rid", rids)
                        .eq("uid", userId));
                if (CollectionUtils.isNotEmpty(hasRoleIds)) {
                    // 设置拥有标记
                    page.getRecords().stream().forEach(roleInfo -> {
                        if (hasRoleIds.contains(roleInfo.getId())) {
                            roleInfo.setChecked(true);
                        }
                    });
                }
            }
        }
        // 查询角色所拥有的权限列表
        else {
            //  分页查询
            page = sysRoleMapper.selectPage(
                    new Query<SysRole>().getPage(curPage, limit),
                    new QueryWrapper<SysRole>()
                            .eq("state", GenericStatus.ENABLED)
                            .like(CommonStringUtils.isNotBlank(roleName), "role_name", CommonStringUtils.isNotBlank(roleName) ? roleName : "")
                            .inSql("id", "select rid from sys_user_role where uid = " + userId)
                            .orderByDesc("id")
            );
        }
        return Result.getInstance().ok().setData(new PageUtils(page));
    }

    /**
     * 用户保存
     *
     * @param user
     * @return
     */
    @Override
    @LogAnnotation(msg = "操作用户信息功能.")
    public Result saveEntity(SysUser user) {
        // 判断权限值是否已经存在
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, user.getUsername())
                .select(SysUser::getId));
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(user.getId())) {
            // 权限值已经存在
            if (sysUser != null) {
                return Result.getInstance().error(-201, "用户已存在,请检查");
            }
            // 当前登录用户
            user.setId(IdWorker.getId());
            user.setCtime(new Date());
            user.setState(GenericStatus.ENABLED);
            user.setSalt(CommonStringUtils.randomHexString(20));
            if (CommonStringUtils.isBlank(user.getPassword())) {
                user.setPassword(ShiroUtils.sha256(DEFAULT_PASSWORD, user.getSalt()));
            }
            // 单独设置了密码
            else {
                user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
            }
            this.save(user);
        }
        // 修改操作
        else {
            if (!CommonStringUtils.equals(sysUser.getId(), user.getId())) {
                return Result.getInstance().error(-201, "用户已存在,请检查");
            }
            this.updateById(user);
        }
        return Result.getInstance().ok().setData(user);
    }

    /**
     * 密码修改
     *
     * @param uid
     * @param password
     * @return
     */
    @Override
    @LogAnnotation(msg = "修改用户密码.")
    public Result resetPassword(@RefIdAnnotation Long uid, String password) {
        // 判断权限值是否已经存在
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getId, uid)
                .select(SysUser::getUsername, SysUser::getSalt, SysUser::getId));
        if (sysUser == null) {
            return Result.getInstance().error(-202, "用户不存在");
        }
        if (CommonStringUtils.isBlank(password)) {
            sysUser.setPassword(ShiroUtils.sha256(DEFAULT_PASSWORD, sysUser.getSalt()));
        }
        // 单独设置了密码
        else {
            sysUser.setPassword(ShiroUtils.sha256(password, sysUser.getSalt()));
        }
        this.updateById(sysUser);
        ShiroUtils.clearAuthCache(sysUser.getUsername());
        return Result.getInstance().ok().setData(sysUser);
    }
}

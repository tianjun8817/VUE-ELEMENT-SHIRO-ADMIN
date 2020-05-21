/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.config.shiro.realm;

import com.admin.config.shiro.utils.ShiroByteSource;
import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.dto.sys.LoginAuthDTO;
import com.admin.mapper.dao.sys.*;
import com.admin.mapper.entity.sys.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 认证
 */
@Component
public class AdminUserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRolePermMapper sysRolePermMapper;
    @Autowired
    private SysPermMapper sysPermMapper;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginAuthDTO dto = (LoginAuthDTO) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (CollectionUtils.isNotEmpty(dto.getPerms())) {
            info.setStringPermissions((Set<String>) dto.getPerms());
        }
        if (CollectionUtils.isNotEmpty(dto.getRoles())) {
            info.setRoles((Set<String>) dto.getRoles());
        }
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        // 获取用户token
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 查询用户信息
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().select("id", "username", "password", "salt")
                .eq("username", token.getUsername()));
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        LoginAuthDTO dto = new LoginAuthDTO(user);
        dto.setUid(user.getId());
        // 查找用户角色相关信息
        List roleIds = sysUserRoleMapper.selectObjs(new QueryWrapper<SysUserRole>().select("rid").eq("uid", user.getId()));
        // 判断角色ID是否为空
        if (CollectionUtils.isNotEmpty(roleIds)) {
            // 查询用户角色信息
            dto.setRoles(sysRoleMapper.selectObjs(new QueryWrapper<SysRole>().select("role_code").in("id", roleIds)));
            // 查询用户所拥有的权限信息
            List permIds = sysRolePermMapper.selectObjs(new QueryWrapper<SysRolePerm>().select("perm_id").in("rid", roleIds));
            // 存在权限值
            if (CollectionUtils.isNotEmpty(permIds)) {
                dto.setPerms(sysPermMapper.selectObjs(new QueryWrapper<SysPerm>().select("perm").in("id", permIds)));
            }
        }
        // 设置认证信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(dto, dto.getSysUser().getPassword(), new ShiroByteSource(dto.getSysUser().getSalt()), getName());
        return info;
    }

    /**
     * 设置验证匹配器
     *
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}

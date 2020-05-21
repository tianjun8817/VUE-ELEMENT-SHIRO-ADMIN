/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys.impl;


import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.constants.GenericStatus;
import com.admin.mapper.dao.sys.SysPermMapper;
import com.admin.mapper.dao.sys.SysRoleMapper;
import com.admin.mapper.entity.sys.SysPerm;
import com.admin.mapper.entity.sys.SysRole;
import com.admin.mapper.entity.sys.SysUser;
import com.admin.result.Result;
import com.admin.aspect.annotation.LogAnnotation;
import com.admin.service.sys.SysPermService;
import com.admin.utils.PageUtils;
import com.admin.utils.Query;
import com.admin.utils.CommonStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 系统用户
 */
@Service("sysPermService")
public class SysPermServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements SysPermService {
    @Autowired
    SysRoleMapper sysRoleMapper;

    /**
     * 分页查询权限信息
     *
     * @param perm
     * @return
     */
    @Override
    public Result queryPage(String perm, long curPage, long limit) {
        //  分页查询
        IPage<SysPerm> page = this.page(
                new Query<SysPerm>().getPage(curPage, limit),
                new QueryWrapper<SysPerm>()
                        .like(CommonStringUtils.isNotBlank(perm), "perm",
                                CommonStringUtils.isNotBlank(perm) ? perm : CommonStringUtils.EMPTY)
                        .orderByDesc("id")
        );
        // 返回结果
        return Result.getInstance().ok().setData(new PageUtils(page));
    }

    /**
     * 添加权限值
     *
     * @param sysPerm
     * @return
     */
    @Override
    @LogAnnotation(msg = "操作权限信息功能.")
    public Result saveEntity(SysPerm sysPerm) {
        sysPerm.setUtime(new Date());
        // 判断权限值是否已经存在
        SysPerm perm = this.getOne(new QueryWrapper<SysPerm>().select("id").eq("perm", sysPerm.getPerm()));
        // 判断ID是否为空
        if (CommonStringUtils.isBlank(sysPerm.getId())) {
            // 权限值已经存在
            if (perm != null) {
                return Result.getInstance().error(-201, "权限值已存在,请检查");
            }
            // 当前登录用户
            SysUser sysuser = ShiroUtils.getUser();
            sysPerm.setId(IdWorker.getId());
            sysPerm.setCtime(new Date());
            sysPerm.setState(GenericStatus.ENABLED);
            sysPerm.setCid(sysuser.getId());
            sysPerm.setCname(sysuser.getUsername());
            this.save(sysPerm);
        }
        // 修改操作
        else {
            if (!CommonStringUtils.equals(perm.getId(), sysPerm.getId())) {
                return Result.getInstance().error(-201, "权限值已存在,请检查");
            }
            this.updateById(sysPerm);
        }
        return Result.getInstance().ok().setData(sysPerm);
    }

    /**
     * 根据权限编号查询角色列表
     *
     * @param permId
     */
    @Override
    public Result queryRolePage(Long permId, long curPage, long limit) {
        IPage<SysRole> page = sysRoleMapper.selectPage(new Query<SysRole>().getPage(curPage, limit), new QueryWrapper<SysRole>().inSql("id",
                "select rid from sys_role_perm where perm_id = " + permId)
        );
        return Result.getInstance().ok().setData(new PageUtils(page));
    }
}

/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys;

import com.admin.mapper.entity.sys.SysPerm;
import com.admin.result.Result;
import com.admin.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 系统用户
 */
public interface SysPermService extends IService<SysPerm> {
    /**
     * 分页查询权限值数据
     *
     * @param perm
     * @param curPage
     * @param limit
     * @return
     */
    Result queryPage(String perm, long curPage, long limit);

    /**
     * 权限值保存
     *
     * @param sysPerm
     * @return
     */
    Result saveEntity(SysPerm sysPerm);

    /**
     * 根据权限编号查询角色列表
     */
    Result<PageUtils> queryRolePage(Long permId, long curPage, long limit);
}

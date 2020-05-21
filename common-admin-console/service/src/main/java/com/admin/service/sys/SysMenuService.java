/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.service.sys;


import com.admin.mapper.entity.sys.SysMenu;
import com.admin.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 系统菜单信息服务类
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取用户菜单列表
     */
    Result<List<SysMenu>> getUserTreeList(Long userId);
}

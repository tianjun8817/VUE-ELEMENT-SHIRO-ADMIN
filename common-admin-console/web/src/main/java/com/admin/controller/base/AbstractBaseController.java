/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.base;

import com.admin.dto.sys.LoginAuthDTO;
import com.admin.mapper.entity.sys.SysUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 */
public abstract class AbstractBaseController {

    /**
     * 获取用户信息
     *
     * @return
     */
    protected LoginAuthDTO getUser() {
        return (LoginAuthDTO) SecurityUtils.getSubject().getPrincipal();
    }
}

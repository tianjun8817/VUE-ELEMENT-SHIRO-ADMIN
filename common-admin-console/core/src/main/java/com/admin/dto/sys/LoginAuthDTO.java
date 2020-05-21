package com.admin.dto.sys;

import com.admin.mapper.entity.sys.SysMenu;
import com.admin.mapper.entity.sys.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户登录信息相关DTO
 *
 * @program: common-admin-console
 * @author: Xuntj
 * @create: 2020-04-13 13:31
 **/
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginAuthDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long uid;
    @NonNull
    SysUser sysUser;
    List perms;
    List roles;
    List<SysMenu> menus;
}

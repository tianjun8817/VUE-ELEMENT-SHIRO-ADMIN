/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.controller.sys;


import com.admin.config.shiro.utils.ShiroUtils;
import com.admin.controller.base.AbstractBaseController;
import com.admin.redis.MenusRDSService;
import com.admin.result.Result;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/api/sys")
public class AdminWebController extends AbstractBaseController {
    @Autowired
    MenusRDSService menusRDSService;

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(String username, String password, boolean remember) {
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            if (remember) {
                token.setRememberMe(true);
            }
            subject.login(token);
        } catch (UnknownAccountException e) {
            return Result.getInstance().error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return Result.getInstance().error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return Result.getInstance().error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return Result.getInstance().error("账户验证失败");
        }
        // 设置用户菜单信息
        return Result.getInstance().ok().setData(ShiroUtils.getUser());
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {
        menusRDSService.delete(String.valueOf(ShiroUtils.getUserId()));
        ShiroUtils.clearAuthCache(ShiroUtils.getUser().getUsername());
        ShiroUtils.logout();
        return Result.getInstance().ok();
    }
}

/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.config.shiro.utils;

import com.admin.config.shiro.realm.AdminUserRealm;
import com.admin.config.shiro.redis.RedisCacheManager;
import com.admin.dto.sys.LoginAuthDTO;
import com.admin.mapper.entity.sys.SysUser;
import com.admin.exception.CommonException;
import com.admin.utils.SpringContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import static com.admin.constants.Constant.AUTHENTICATION_CACHE;

/**
 * Shiro工具类
 */
public class ShiroUtils {
    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";
    /**
     * 循环次数
     */
    public final static int hashIterations = 16;

    static AdminUserRealm shiroRealm = SpringContextUtils.getBean("shiroRealm", AdminUserRealm.class);

    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static LoginAuthDTO getLoginAuthDTO() {
        return (LoginAuthDTO) SecurityUtils.getSubject().getPrincipal();
    }

    public static SysUser getUser() {
        return getLoginAuthDTO().getSysUser();
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        Object kaptcha = getSessionAttribute(key);
        if (kaptcha == null) {
            throw new CommonException("验证码已失效");
        }
        getSession().removeAttribute(key);
        return kaptcha.toString();
    }

    public static void clearAuthCache(String key) {
        Cache<Object, ?> cache = shiroRealm.getAuthenticationCache();
        if (cache != null) {
            cache.remove(key);
        }
    }

}

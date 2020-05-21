/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.constants;

/**
 * Redis所有Keys
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }

    public static String getShiroSessionKey(String key){
        return "sessionid:" + key;
    }
}

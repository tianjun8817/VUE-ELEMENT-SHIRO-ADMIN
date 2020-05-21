/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */
package com.admin.aspect.annotation;

import java.lang.annotation.*;

/**
 * @program: common-admin-console
 * @description: 日志记录枚举
 * @author: Mr.Wang
 * @create: 2020-04-11 19:58
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefIdAnnotation {
}

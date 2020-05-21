/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 *
 */

package com.admin.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}

package com.admin.config.shiro.redis;

import lombok.Data;
import org.apache.shiro.session.Session;

import java.util.Date;

/**
 * Use ThreadLocal as a temporary storage of Session, so that shiro wouldn't keep read redis several times while a request coming.
 *
 * @program: common-admin
 * @author: Xuntj
 * @create: 2020-03-22 22:25
 **/
@Data
public class SessionInMemory {
    private Session session;
    private Date createTime;
}

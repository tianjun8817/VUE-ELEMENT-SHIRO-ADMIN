package com.admin.redis.impl;

import com.admin.redis.MenusRDSService;
import com.admin.config.redis.BaseRepositoryImpl;
import org.springframework.stereotype.Service;

/**
 * 菜单信息相关redis缓存实现类
 *
 * @program: common-admin-console
 * @author: Xuntj
 * @create: 2020-03-27 23:20
 **/
@Service
public class MenusRDSServiceImpl extends BaseRepositoryImpl<Object> implements MenusRDSService {

    @Override
    protected String getPrefix() {
        return "MENU";
    }
}

package com.admin.dto.sys;

import com.admin.service.sys.SysUserService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 菜单信息测试类
 *
 * @program: common-admin-console
 * @author: Xuntj
 * @create: 2020-04-07 14:51
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysMenuTest {
    @Autowired
    SysUserService sysUserService;

    @Test
    public void menu_test_run() {
        System.out.println(JSON.toJSONString(sysUserService.queryRoleIdList(2l)));
    }

}

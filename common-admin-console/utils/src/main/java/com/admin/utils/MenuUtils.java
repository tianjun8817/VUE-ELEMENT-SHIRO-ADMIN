package com.admin.utils;

import com.admin.mapper.entity.sys.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单信息相关工具类
 *
 * @program: common-admin-console
 * @author: Xuntj
 * @create: 2020-03-27 18:16
 **/
public class MenuUtils {
    /**
     * 递归
     */
    public static List<SysMenu> getMenuTreeList(List<SysMenu> menuList) {
        List<SysMenu> treeMenu = new ArrayList<>();
        menuList.forEach(menu -> {
            // 当前封装的菜单是否为空
            if (CommonStringUtils.equals(menu.getPid(), 0)) {
                menu.setChildren(new ArrayList<>());
                treeMenu.add(menu);
                // 执行下一次循环
                return;
            }
        });
        // 移除已处理菜单
        menuList.removeAll(treeMenu);
        // 当前菜单存在值,循环查找子菜单
        treeMenu.forEach(tree -> {
            // 判断当前菜单pid是否已经存在于父级菜单中
            tree.setChildren(getChild(tree, menuList));
        });
        return treeMenu;
    }

    /**
     * 获取子节点
     *
     * @param parent  父节点
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public static List<SysMenu> getChild(SysMenu parent, List<SysMenu> allMenu) {
        //子菜单
        List<SysMenu> childList = new ArrayList<>();
        //
        allMenu.forEach(nav -> {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (CommonStringUtils.equals(parent.getId(), nav.getPid())) {
                childList.add(nav);
            }
        });
        //递归
        childList.forEach(nav -> {
            nav.setChildren(getChild(nav, allMenu));
        });
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }


}

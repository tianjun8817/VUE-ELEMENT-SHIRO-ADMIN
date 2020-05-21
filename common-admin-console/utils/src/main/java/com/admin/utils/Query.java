/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */

package com.admin.utils;

import com.admin.constants.Constant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询参数
 */
public class Query<T> {

    /**
     * 创建分页对象
     *
     * @param curPage   当前页数
     * @param limit     每页条数
     * @param orderItem 分页对象
     * @return
     */
    public IPage<T> getPage(long curPage, long limit, OrderItem... orderItem) {
        //分页对象
        Page<T> page = new Page<>(curPage, limit);
        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        //前端字段排序
        if (orderItem != null && orderItem.length > 0) {
            return page.addOrder(orderItem);
        }
        return page;
    }
}

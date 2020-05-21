/**
 * Copyright (c) 2020-2020 通用后台 All rights reserved.
 */
package com.admin.aspect.action;

import com.admin.aspect.annotation.LogAnnotation;
import com.admin.aspect.annotation.RefIdAnnotation;
import com.admin.aspect.constant.LogType;
import com.admin.dto.sys.LoginAuthDTO;
import com.admin.mapper.dao.log.OperLogMapper;
import com.admin.mapper.entity.log.OperLog;
import com.admin.utils.CommonStringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static com.alibaba.fastjson.JSON.DEFFAULT_DATE_FORMAT;

/**
 * @description: 操作信息日志记录
 * @program: common-admin-console
 * @author: tianjun87001@163.com
 * @create: 2020-04-11 20:15
 **/
@Aspect
@Service
public class LogAspect {
    @Autowired
    OperLogMapper operLogMapper;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.admin.aspect.annotation.LogAnnotation)")
    public void pointCut() {
    }

    /**
     * 日期后置拦截
     */
    @AfterReturning(value = "pointCut() && @annotation(logAnnotation)", returning = "ret")
    public void after(JoinPoint joinPoint, LogAnnotation logAnnotation, Object ret) {
        // 操作日志记录
        OperLog log = new OperLog();
        log.setId(IdWorker.getId());
        LoginAuthDTO dto = (LoginAuthDTO) SecurityUtils.getSubject().getPrincipal();
        log.setUid(dto.getSysUser().getId());
        log.setUname(dto.getSysUser().getUsername());
        log.setMsg(logAnnotation.msg());
        // 记录返回结果
        if (logAnnotation.recordRet()) {
            JSONObject json = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(ret, DEFFAULT_DATE_FORMAT));
            log.setRes(json.toJSONString());
            // 判断是否存在关联ID
            if (json.containsKey("id")) {
                log.setRefId(json.getLong("id"));
            }
            // 判断是否存在data结果
            else if (json.containsKey("data")) {
                JSONObject data = json.getJSONObject("data");
                if (data.containsKey("id")) {
                    log.setRefId(data.getLong("id"));
                }
            }
        }
        // 记录请求参数
        else {
            log.setRes(JSONArray.toJSONString(joinPoint.getArgs()));
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
                if (arg != null && arg.getClass().isAnnotationPresent(RefIdAnnotation.class)) {
                    log.setRefId(Long.valueOf(String.valueOf(arg)));
                }
            });
        }
        // 未设置类型,默认为操作数据
        if (CommonStringUtils.isBlank(logAnnotation.type())) {
            log.setType(LogType.OPERATE.name());
        }
        log.setCtime(new Date());
        operLogMapper.insert(log);
    }

}

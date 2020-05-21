package com.admin.config.shiro.core;

import com.admin.config.redis.RedisManager;
import com.admin.config.shiro.redis.RedisSessionDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 解决单次请求需要多次访问redis
 *
 * @program: common-admin
 * @author: Xuntj
 * @create: 2020-03-22 22:29
 **/
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {
    /**
     * 获取session
     * 优化单次请求需要多次访问redis的问题
     *
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                return (Session) sessionObj;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        /* 此方法获取客户端cookie的值,如果你的项目将sesssionId放在requestparam中，或者拼接在url中，请查看该方法源码，自行修改*/
        String id = super.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        if (id != null) {
            /*此处并非http 的session，是shiro在redis中缓存的session（SimpleSession）*/
            /* 此方法是查询redis中的session，笔者在sessionDao中注入了redisManage如果你重写了RedisSessionDAO，则需要更改获取session的方法 */
            Session session = this.sessionDAO.readSession(id);
            if (session == null) {
                return null;
            }
        }
        /*   如果redis中session未过期，此处必须调用父类获取方法 */
        return super.getSessionId(request, response);
    }
}

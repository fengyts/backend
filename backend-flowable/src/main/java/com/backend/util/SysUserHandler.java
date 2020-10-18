package com.backend.util;

import com.backend.model.entity.SysUserEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 系统登陆, 当前用户会话上下文
 *
 * @author fengyts
 */
@Slf4j
public class SysUserHandler {

    private static final String LOGIN_MSG = "登陆失败：用户为空";

    /**
     * 登陆会话session的Key
     */
    private static final String SYS_USER_SESSION_KEY_PREFIX = "session_login:sysuser_";
    /**
     * session有效期：30分钟, 单位:秒
     */
    private static final int SESSION_EXPIRES = 60 * 30;

    /*private static ThymeleafViewResolver viewResolver;

    public static void initThymeleafViewResolver(ThymeleafViewResolver viewResolver) {
        SysUserHandler.viewResolver = viewResolver;
    }*/

    public static SysUserEntity getCurrentUser() {
        SysUserEntity sysUserEntity = (SysUserEntity) getHttpSession(Boolean.TRUE).getAttribute(SYS_USER_SESSION_KEY_PREFIX);
        log.info("current login user: {}", sysUserEntity);
        return sysUserEntity;
    }

    /**
     * 用户登陆
     *
     * @param sysUser
     */
    public static void userLogin(SysUserEntity sysUser) {
        if (null == sysUser) {
            log.info(LOGIN_MSG);
            throw new IllegalArgumentException(LOGIN_MSG);
        }
        getHttpSession(Boolean.TRUE).setAttribute(SYS_USER_SESSION_KEY_PREFIX, sysUser);

        // 登陆时 处理flowable-modeler-ui的用户登陆
        ModelerUISecurityUtil.initAssumeUser();
    }

    /**
     * 用户退出系统
     */
    public static void userLogout() {
        getHttpSession(Boolean.FALSE).invalidate();
    }

    public static void renewSessionExpires() {
        getHttpSession(Boolean.TRUE).setMaxInactiveInterval(SESSION_EXPIRES);
    }

    private static HttpSession getHttpSession(boolean create) {
        return getHttpServletRequest().getSession(create);
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) requestAttr;
        HttpServletRequest request = servletReqAttr.getRequest();
        return request;
    }

}

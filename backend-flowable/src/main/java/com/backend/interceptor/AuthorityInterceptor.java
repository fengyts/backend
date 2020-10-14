package com.backend.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.backend.annotion.NonAutority;
import com.backend.common.ResultData;
import com.backend.common.SysConstant;
import com.backend.model.entity.SysUserEntity;
import com.backend.util.SysUserHandler;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义拦截器 实现登陆
 * @author DELL
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final String URI_FLOWABLE_LOGIN = "/app/rest/account";
    private static final String URI_FLOWABLE_MODELS = "/app/rest/models";
    private static final String CHARSET_UTF8_DEFAULT = "UTF-8";

    /**
     * 不拦截的url
     */
    private static final Set<String> EXCLUSIONS_URI = Sets.newHashSet();

    static {
        EXCLUSIONS_URI.add(SysConstant.URI_LOGIN);
        EXCLUSIONS_URI.add(SysConstant.URI_KAPTCHA);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        // 拦截退出操作
        if (SysConstant.URI_LOGIN_OUT.equals(requestURI)) {
            SysUserHandler.userLogout();
            response.sendRedirect(SysConstant.URI_LOGIN);
            return false;
        }
        if (EXCLUSIONS_URI.contains(requestURI)) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> clazz = handlerMethod.getBeanType();
            // 过滤无需鉴权的请求, 这里使用注解方式, 也可以在WebMvcConfig中配置
            NonAutority unAuthority = handlerMethod.getMethodAnnotation(NonAutority.class);
            if (null != unAuthority) {
                return true;
            }
            SysUserEntity currentUser = SysUserHandler.getCurrentUser();
            if (null == currentUser) {
                log.info("401: 用户未登陆或登陆过期, uri: {}", requestURI);
                if (URI_FLOWABLE_LOGIN.equals(requestURI)) {
                    handleFlowableModelerUnLogin(response);
                } else {
                    response.sendRedirect(SysConstant.URI_LOGIN);
                }
                return false;
            } else {
                SysUserHandler.renewSessionExpires();
                return true;
            }
        }
//        return false;
        return true;
    }

    private void handleFlowableModelerUnLogin(HttpServletResponse response) {
        // response.setStatus(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(CHARSET_UTF8_DEFAULT);
        try (
                ServletOutputStream outputStream = response.getOutputStream();
        ) {
            ResultData resultData = ResultData.okOfMapData()
                    .extendMapData("status", HttpStatus.UNAUTHORIZED.value())
                    .extendMapData("sessionOut", true)
                    .extendMapData("redirectToLogin", SysConstant.URI_LOGIN);
            byte[] bytes = JSONObject.toJSONBytes(resultData);

            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            log.info("服务器返回异常：{}", e);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}

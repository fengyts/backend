package com.backend.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.backend.annotion.NonAutority;
import com.backend.common.ResultData;
import com.backend.common.SysConstant;
import com.backend.model.entity.SysUserEntity;
import com.backend.util.SysUserHandler;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义拦截器 实现登陆
 *
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
                if (SysConstant.URI_INDEX.equals(requestURI)) {
                    log.info("current login user: {}", currentUser);
                }
                return true;
            }
        }
//        return false;
        return true;
    }

    /**
     * 处理flowable-modeler-ui集成后的登陆问题
     * 需要在 flowable-modeler/scripts/app.js文件中做修改(在第329行，getAccountUrl() 函数调用中增加error()处理)：
     * {@code
     * $http.get(FLOWABLE.APP_URL.getAccountUrl())
     * .success(function (data, status, headers, config) {
     * $rootScope.account = data;
     * $rootScope.invalidCredentials = false;
     * $rootScope.authenticated = true;
     * }).error(function (res, status, headers, config) {
     * if (res.code === '0') {
     * let data = res.data;
     * if (401 === status) {
     * // $window.location.href = "/login";
     * $window.location.href = data['redirectToLogin'];
     * }
     * }
     * });
     * }
     *
     * @param response
     */
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
        configureThymeleafGloableVars(modelAndView);
    }

    /**
     * 给thymeleaf 模板设置全局登陆用户的变量,在common/common_header.html中的script中接收为全局变量
     *
     * @param modelAndView
     */
    private static void configureThymeleafGloableVars(ModelAndView modelAndView) {
        try {
            SysUserEntity currentUser = SysUserHandler.getCurrentUser();
            if (null == modelAndView || null == currentUser) {
                return;
            }
            ModelMap modelMap = modelAndView.getModelMap();
            Object obj = modelMap.getAttribute(SysConstant.THYMELEAF_SYSUSER_KEY);
            if (null == obj) {
                modelAndView.addObject(SysConstant.THYMELEAF_SYSUSER_KEY, currentUser);
                return;
            }
            if (obj instanceof SysUserEntity) {
                SysUserEntity sysUser = (SysUserEntity) obj;
                if (sysUser.getId().equals(currentUser.getId())) {
                    modelAndView.addObject(SysConstant.THYMELEAF_SYSUSER_KEY, currentUser);
                }
            } else {
                log.info("处理加载登陆用户信息：thymeleaf全局变量->sysUser<-值被覆盖, 它不是SysUserEntity的实例");
            }
        } catch (Exception e) {
            log.info("用户未登陆或登陆超时, postHandle -> ModelAndView 处理加载登陆用户信息异常, {}", e);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}

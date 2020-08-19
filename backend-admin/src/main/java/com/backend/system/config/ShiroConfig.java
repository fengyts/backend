/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.backend.system.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.backend.system.shiro.ShiroConstant;
import com.backend.system.shiro.SysAuthorizingRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        // 自定义过滤器
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("oauth2", new OAuth2Filter());
//        shiroFilter.setFilters(filters);

        shiroFilter.setLoginUrl("/login"); //登录
        shiroFilter.setSuccessUrl("/index"); //首页
        shiroFilter.setUnauthorizedUrl("/403"); //错误页面，认证不通过跳转

        // 设置路径映射，注意这里要用LinkedHashMap 保证有序
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/plugins/**", "anon");
//        filterMap.put("/js/**", "anon");
//        filterMap.put("/css/**", "anon");
        filterMap.put("/common/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
//        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/kaptcha/**", "anon");

        filterMap.put("/logout", "logout");
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    /**
     * web应用管理配置
     *
     * @param shiroRealm
     * @param cacheManager
     * @param manager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(Realm shiroRealm, CacheManager cacheManager, RememberMeManager manager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(manager);//记住Cookie
        securityManager.setRealm(shiroRealm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * session过期控制
     *
     * @return
     * @author fuce
     * @Date 2019年11月2日 下午12:49:49
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        // 设置session过期时间3600s
//        Long timeout = 60L * 1000 * 60;//毫秒级别
        Long timeout = 60L * 1000 * 30;
        defaultWebSessionManager.setGlobalSessionTimeout(timeout);
        return defaultWebSessionManager;
    }

    /**
     * 加密算法
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5"); //采用MD5 进行加密
        hashedCredentialsMatcher.setHashIterations(ShiroConstant.HASH_ITERATIONS); //加密次数
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true); // hex or base64
        return hashedCredentialsMatcher;
    }

    /**
     * 记住我的配置
     *
     * @return
     */
    @Bean
    public RememberMeManager rememberMeManager() {
        Cookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true); //通过js脚本将无法读取到cookie信息
        cookie.setMaxAge(60 * 60 * 24); //cookie保存一天
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(cookie);
        return manager;
    }

    /**
     * 缓存配置
     *
     * @return
     */
    @Bean
    public CacheManager shiroCacheManager() {
        MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();//使用内存缓存
        return cacheManager;
    }

    /**
     * 配置realm，用于认证和授权
     *
     * @param hashedCredentialsMatcher
     * @return
     */
    @Bean
    public AuthorizingRealm shiroRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        SysAuthorizingRealm shiroRealm = new SysAuthorizingRealm();
        //校验密码用到的算法
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return shiroRealm;
    }

    /**
     * 启用shiro方言，这样能在页面上使用shiro标签
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 启用shiro注解
     * 加入注解的使用，不加入这个注解不生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}

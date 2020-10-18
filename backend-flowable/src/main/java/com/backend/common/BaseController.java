package com.backend.common;

import com.backend.model.entity.SysUserEntity;
import com.backend.util.SysUserHandler;
import org.springframework.ui.Model;

/**
 * @author fengyts
 */
public abstract class BaseController {

    protected SysUserEntity getCurrentLoginUser(){
        return SysUserHandler.getCurrentUser();
    }

    /**
     * 无需手动设置
     * 统一在 {@link com.backend.interceptor.AuthorityInterceptor}的preHandle方法中处理
     *
     * @param model
     */
    @Deprecated
    protected void setCurrentUser(Model model) {
        model.addAttribute(SysConstant.THYMELEAF_SYSUSER_KEY, getCurrentLoginUser());
    }

}

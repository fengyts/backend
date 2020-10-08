package com.backend.common;

import com.backend.model.entity.SysUserEntity;
import com.backend.util.SysUserHandler;
import org.springframework.ui.Model;

public abstract class BaseController {

    protected SysUserEntity getCurrentLoginUser(){
        return SysUserHandler.getCurrentUser();
    }

    protected void setCurrentUser(Model model) {
        model.addAttribute("sysUser", getCurrentLoginUser());
    }

}

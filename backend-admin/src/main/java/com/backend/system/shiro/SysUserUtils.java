package com.backend.system.shiro;

import com.backend.system.entity.SysUser;
import org.apache.shiro.SecurityUtils;

public class SysUserUtils {

    public static SysUser getCurrentUser() {
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return principal;
    }

}

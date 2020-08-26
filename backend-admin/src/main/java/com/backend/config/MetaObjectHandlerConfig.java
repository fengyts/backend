package com.backend.config;

import com.backend.system.entity.SysUser;
import com.backend.system.shiro.SysUserUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    private SysUser getCurrentUser(){
        return SysUserUtils.getCurrentUser();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        now.withNano(0);
        setFieldValByName("createTime", now, metaObject);
        setFieldValByName("modifyTime", now, metaObject);
        Long userId = getCurrentUser().getId();
        setFieldValByName("createUserId", userId, metaObject);
        setFieldValByName("modifyUserId", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        now.withNano(0);
        setFieldValByName("modifyTime", now, metaObject);
        Long userId = getCurrentUser().getId();
        setFieldValByName("modifyUserId", userId, metaObject);
    }

}

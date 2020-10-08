package com.backend.service;

import com.backend.model.entity.SysUserEntity;

public interface IUserService {

    SysUserEntity selectUserByLoginName(String loginName);

}

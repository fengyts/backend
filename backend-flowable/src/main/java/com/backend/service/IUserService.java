package com.backend.service;

import com.backend.model.entity.SysUserEntity;
import java.util.List;

public interface IUserService {

    SysUserEntity selectUserByLoginName(String loginName);

    List<SysUserEntity> listAllUsers();

}

package com.backend.service.impl;

import com.backend.mock.SysUserMock;
import com.backend.model.entity.SysUserEntity;
import com.backend.service.IUserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Override
    public SysUserEntity selectUserByLoginName(String loginName) {
        return null;
    }

    @Override
    public List<SysUserEntity> listAllUsers() {
        SysUserMock mock = new SysUserMock();
        List<SysUserEntity> users = mock.getSysUsers();
        return users;
    }

}

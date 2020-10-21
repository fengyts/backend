package com.backend.service.impl;

import com.backend.mock.SysUserMock;
import com.backend.model.entity.SysUserEntity;
import com.backend.service.IUserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private List<SysUserEntity> getAllSysUsers() {
        SysUserMock mock = new SysUserMock();
        List<SysUserEntity> users = mock.getSysUsers();
        return users;
    }

    @Override
    public SysUserEntity selectUserByLoginName(String loginName) {
        return null;
    }

    @Override
    public List<SysUserEntity> listAllUsers() {
        List<SysUserEntity> users = getAllSysUsers();
        return users;
    }

    @Override
    public SysUserEntity selectById(Long userId) {
        if(null == userId || 0 < userId.longValue()){}
        List<SysUserEntity> users = getAllSysUsers();
        SysUserEntity sysUserEntity = users.stream()
                .filter(u -> userId.longValue() == u.getId().longValue())
                .collect(Collectors.toList()).get(0);
        return sysUserEntity;
    }

}

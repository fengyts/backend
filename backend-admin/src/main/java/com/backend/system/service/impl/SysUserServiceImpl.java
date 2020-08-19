package com.backend.system.service.impl;

import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysUser;
import com.backend.system.mapper.SysUserMapper;
import com.backend.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserDao;

    @Override
    public SysUser selectByUserName(String username) {
        SysUser sysUser = baseMapper.selectByUserName(username);
        return sysUser;
    }

    @Override
    public List<SysRole> findRolesByUserName(String userName) {
        List<SysRole> roleList = baseMapper.findRolesByUserName(userName);
        return roleList;
    }

    @Override
    public List<SysMenu> selectMenusByRoleId(Long roleId) {
        List<SysMenu> menuList = baseMapper.selectMenusByRoleId(roleId);
        return menuList;
    }

    @Override
    public Page<SysUser> queryPageList(Page<SysUser> webPageQuery) {
        Page<SysUser> page = new Page<>();
        IPage<SysUser> iPage = baseMapper.selectSysUserPageVo(webPageQuery);
        return (Page<SysUser>) iPage;

//        List<SysUser> records = this.list();
//        page.setRecords(records);
//        page.setTotal(1);
//        page.setCurrent(1);
//        page.setSize(10);
//        page.setPages(1);
//        return page;
    }

}

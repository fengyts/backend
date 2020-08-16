package com.backend.system.service;

import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser selectByUserName(String username);

    List<SysRole> findRolesByUserName(String userName);

    List<SysMenu> selectMenusByRoleId(Long roleId);

    Page<SysUser> queryPageList(Page<SysUser> webPageQuery);


}

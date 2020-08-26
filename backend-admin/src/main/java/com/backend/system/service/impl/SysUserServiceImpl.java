package com.backend.system.service.impl;

import com.backend.common.Constants;
import com.backend.common.ResultData;
import com.backend.enums.SysUserTypeEnum;
import com.backend.system.converter.SysUserConverter;
import com.backend.system.dto.SysUserDto;
import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysUser;
import com.backend.system.mapper.SysUserMapper;
import com.backend.system.service.ISysUserService;
import com.backend.system.shiro.ShiroUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

//    @Autowired
//    private SysUserMapper sysUserDao;

    @Autowired
    private SysUserConverter converter;

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
    public Page<SysUserDto> queryPageList(Page<SysUserDto> webPageQuery) {
        Page<SysUserDto> page = new Page<>();
        IPage<SysUserDto> iPage = baseMapper.selectSysUserPageVo(webPageQuery);
        return (Page<SysUserDto>) iPage;
    }

    @Override
    public SysUserDto selectOneByUserId(Long userId) {
        SysUser sysUser = baseMapper.selectById(userId);
        SysUserDto dto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, dto);
        return dto;
    }

    @Override
    public ResultData saveOrUpdate(SysUserDto sysUserDto) {
        SysUser sysUser = converter.toEntity(sysUserDto);
        Long id = sysUserDto.getId();
        // 新增用户
        // 校验是否存在
        // 设置初始密码
        if(null == id){
            SysUser userDb = this.selectByUserName(sysUserDto.getUserName());
            if (!Objects.isNull(userDb)) {
                return ResultData.err("用户名已存在");
            }
            String initialPwd = Constants.SYS_USER_INITIAL_PWD;
            String pwd = ShiroUtil.encryptPasswdShiro(initialPwd);
            sysUser.setPasswd(pwd);
        }
        sysUser.setType(SysUserTypeEnum.SYSTEM.getCode());
        boolean res = super.saveOrUpdate(sysUser);
        return res ? ResultData.ok(): ResultData.errSystem();
    }

}

package com.backend.system.service;

import com.backend.common.ResultData;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRoleDto> listRoles();

    SysRoleDto selectById(Long id);

    ResultData saveOrUpdate(SysRoleDto dto);

}

package com.backend.system.dto;

import com.backend.common.BaseDto;
import lombok.Data;

@Data
public class SysRoleDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态(是否可用): 1-可用；0-不可用
     */
    private Integer status;

}

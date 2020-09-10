package com.backend.system.dto;

import com.backend.common.BaseDto;
import lombok.Data;

/**
 * @author DELL
 */

@Data
public class SysRoleMenuDto extends SysMenuDto {

    private static final long serialVersionUID = 1L;

    private Boolean checked;

}

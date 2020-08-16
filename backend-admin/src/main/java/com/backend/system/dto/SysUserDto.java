package com.backend.system.dto;

import com.backend.common.BaseDto;
import lombok.Data;

@Data
public class SysUserDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String nickName;
    private String mobile;
    private String phone;
    private String email;
    private Integer sex;
    private Integer type;
    private Integer status;

}

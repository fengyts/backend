package com.backend.system.dto;

import com.backend.common.BaseDto;
import lombok.Data;

@Data
public class LoginDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String passwd;
    private Boolean rememberMe;

}

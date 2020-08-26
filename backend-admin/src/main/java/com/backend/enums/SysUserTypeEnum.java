package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SysUserTypeEnum {
    
    /**/
    SYSTEM(0, "系统用户"),
    BUSINESS(1, "业务用户"),
    OTHER(2, "其他用户");

    private final Integer code;
    private final String desc;

}

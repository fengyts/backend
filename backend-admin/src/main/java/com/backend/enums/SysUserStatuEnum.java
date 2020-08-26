package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SysUserStatuEnum {

    /**/
    NORMAL(1, "正常"),
    FREEZE(0, "冻结");

    private final Integer code;
    private final String desc;

}

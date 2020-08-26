package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别枚举类
 */
@Getter
@RequiredArgsConstructor
public enum SexEnum {

    /* */
    MAN(1, "男"),
    WOMAN(0, "女");

    private final Integer code;
    private final String desc;

}

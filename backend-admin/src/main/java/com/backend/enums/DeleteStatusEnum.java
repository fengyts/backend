package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 是否删除：0-未删除；1-已删除
 */

@Getter
@RequiredArgsConstructor
public enum DeleteStatusEnum {

    DELETE_TRUE(1, "删除"),
    DELETE_FALSE(0, "未删除");

    private final int code;
    private final String desc;

}

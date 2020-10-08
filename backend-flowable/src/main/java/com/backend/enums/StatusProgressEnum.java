package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 流程类型流程状态枚举类
 */
@Getter
@RequiredArgsConstructor
public enum StatusProgressEnum {

    UNCOMMITTED(0, "待提交"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成");

    private final int code;
    private final String desc;

}

package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 排他网关：通过/驳回 枚举类
 */
@Getter
@RequiredArgsConstructor
public enum AuditTypeEnum {

    PASS("pass", "通过"),
    REJECT("reject", "驳回");

    private final String code;
    private final String desc;

}

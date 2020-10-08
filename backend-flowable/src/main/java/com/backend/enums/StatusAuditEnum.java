package com.backend.enums;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 审批类型流程状态枚举类
 */
@Getter
@RequiredArgsConstructor
public enum StatusAuditEnum {

    UNCOMMITTED(0, "待提交"),
    UNDER_REVIEW(1, "审核中"),
    FINISHED(2, "已结束"),

    REJECTED(3, "已驳回"),
    CANCEL(4, "已取消"),
    TIME_OUT(5, "已过期"),
    CLOSED(6, "已关闭");

    private final int code;
    private final String desc;

    public static Map<String, String> statusPairValue (){
        Map<String, String> map = Maps.newHashMap();
        StatusAuditEnum[] vs = StatusAuditEnum.values();
        for (StatusAuditEnum v : vs) {
            map.put(v.name(), v.desc);
        }
        return map;
    }

}

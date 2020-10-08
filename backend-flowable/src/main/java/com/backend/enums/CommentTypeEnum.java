package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentTypeEnum {
    SP("SP", "审批"),
    BH("HB", "驳回"),
    CH("CH", "撤回"),
    ZC("ZC", "暂存"),
    QS("QS", "签收"),
    WP("WP", "委派"),
    ZH("ZH", "知会"),
    ZY("ZY", "转阅"),
    YY("YY", "已阅"),
    ZB("ZB", "转办"),
    QJQ("QJQ", "前加签"),
    HJQ("HJQ", "后加签"),
    XTZX("XTZX", "系统执行"),
    TJ("TJ", "提交"),
    CXTJ("CXTJ", "重新提交"),
    SPJS("SPJS", "审批结束"),
    LCZZ("LCZZ", "流程终止"),
    SQ("SQ", "授权"),
    CFTG("CFTG", "重复跳过"),
    XT("XT", "协同"),
    PS("PS", "评审");

    private final String code;
    private final String desc;

    /**
     * 通过code获取desc
     *
     * @param code
     * @return
     * @Description:
     */
    /*public static String getDescByCode(String code) {
        for (CommentTypeEnum e : CommentTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return "";
    }*/

}
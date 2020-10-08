package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 请假类型枚举类
 * @author fengyts
 */
@Getter
@RequiredArgsConstructor
public enum HolidayTypeEnum {

    PERSONAL_LEAVE(1, "事假"),
    SICK_LEAVE(2, "病假"),
    MARRIAGE_LEAVE(3, "婚假"),
    MATERNITY_LEAVE(4, "产假"),
    PRENATAL_CHECKUP_LEAVE(5, "产检假"),
    PATERNITY_LEAVE(6, "陪产假"),
    BREASTFEEDING_LEAVE(7, "哺乳假"),
    FUNERAL_LEAVE(8, "丧假"),
    WORKRELATED_INJURY_LEAVE(9, "工伤假"),
    DAYS_OFF_LEAVE(10, "调休"),
    ANNUAL_VACATION(11, "年假");


    private final int code;
    private final String desc;

}

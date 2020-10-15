package com.backend.common;

/**
 * 常用校验正则表达式常量
 * @author DELL
 */
public final class RegexConstant {

    /** 手机号 */
    public static final String MOBILE = "^0*1[3-9]\\d{9}$";
    /** Email */
    public static final String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /** QQ号码 */
    public static final String QQ_NO = "^[1-9]*[1-9][0-9]*$";
    /** 整数 */
    public static final String INTEGER = "^\\d+$";
    /** 负整数 */
    public static final String NEGATIVE_INTEGER = "^-{1}d+$";
    /** 非负整数(0和正整数) */
    public static final String POSITIVE_INTEGER = "^(0|[1-9]d*)$";
    /** 英文字母 */
    public static final String EN_LETTER = "^[a-zA-Z]+$";
    /** 英文字母和数字 */
    public static final String LETTER_NUM = "^[a-zA-Z0-9]+$";
    /** 汉字 */
    public static final String CHINESE_CHAR = "^[\\u4e00-\\u9fa5]+$";
    /** IP地址 */
    public static final String IP = "^([0-9]{1,3}.{1}){3}[0-9]{1,3}$";

}

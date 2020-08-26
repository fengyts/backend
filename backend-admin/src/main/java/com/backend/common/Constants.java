package com.backend.common;

public final class Constants {

    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SECONDS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLI_SECONDS = "yyyy-MM-dd HH:mm:ss SSSS";
    public static final String MYSQL_TO_CHAR_MILLI_SECONDS_FORMAT4 = "concat(date_format(modify_time, '%Y-%m-%d %H:%i:%s'),' 0000') ";

    /* 系统用户初始密码 */
    public static final String SYS_USER_INITIAL_PWD = "123456";

}

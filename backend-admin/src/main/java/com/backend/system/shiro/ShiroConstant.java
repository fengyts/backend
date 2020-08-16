package com.backend.system.shiro;

public class ShiroConstant {

    /**
     * 新用户默认密码
     */
    public static final String DEFAULT_PASSWD = "123456";

    /**
     * 默认编码格式 utf8
     */
    public static String DEFAULT_ENCODING = "UTF-8";

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";

    /**
     * 加密次数
     */
    public static final int HASH_ITERATIONS = 1024;
    /**
     * salt的长度
     */
    public static final int SALT_SIZE16 = 16;
    public static final int SALT_SIZE8 = 8;

}

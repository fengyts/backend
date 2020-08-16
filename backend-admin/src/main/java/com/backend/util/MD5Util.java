package com.backend.util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static String encrypt32md5(String str) {
        String md5Hex = DigestUtils.md5Hex(str);
        return md5Hex;
    }

    public static String encrypt16md5(String str) {
        return encrypt32md5(str).substring(8, 24);
    }


}

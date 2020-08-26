package com.backend.util;

import com.backend.common.Regexps;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidatorUtil {

    /**
     * 校验手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(Regexps.MOBILE);
        m = p.matcher(mobile);
        b = m.matches();
        return b;
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        if (email.length() > 100) {
            return false;
        }
        String regexEmail = Regexps.EMAIL;
        return Pattern.matches(regexEmail, email);
    }

}

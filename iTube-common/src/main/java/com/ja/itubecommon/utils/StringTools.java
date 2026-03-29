package com.ja.itubecommon.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

public class StringTools {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 获取随机字符串(纯数字和字母)
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    /**
     * 获取随机字符串(纯数字)
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandomNumber(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static String encodeByMd5(String str) {
    	return StringTools.isEmpty(str) ? null : DigestUtils.md5DigestAsHex(str.getBytes());
    }
}

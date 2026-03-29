package com.ja.itubecommon.entity.constants;

public class Constants {
    public static final Integer LENGTH_USER_ID = 10;    // 用户ID长度
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";  // 密码正则
    public static final Integer REDIS_KEY_EXPIRE_ONE_MIN = 60000;   // redis key 过期时间 1分钟
    public static final String REDIS_KEY_PREFIX = "itube:";     // redis key 前缀
    public static final String REDIS_KEY_VERIFY_CODE = "verifyCode:";   // 验证码在redis中的key的前缀
}

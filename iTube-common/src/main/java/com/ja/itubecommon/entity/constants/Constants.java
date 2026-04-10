package com.ja.itubecommon.entity.constants;

public class Constants {
    public static final Integer LENGTH_USER_ID = 10;    // 用户ID长度
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";  // 密码正则
    public static final Integer REDIS_KEY_EXPIRE_ONE_MIN = 60000;   // redis key 过期时间 1分钟
    public static final Integer REDIS_KEY_EXPIRE_ONE_DAY = REDIS_KEY_EXPIRE_ONE_MIN * 60 * 24;    // redis key 过期时间 1天
    public static final Integer TIME_ONT_DAY_TO_SECOND = REDIS_KEY_EXPIRE_ONE_DAY / 1000;

    public static final String REDIS_KEY_PREFIX = "itube:";     // redis key 前缀
    public static final String REDIS_KEY_VERIFY_CODE = REDIS_KEY_PREFIX + "verifyCode:";   // 验证码在redis中的key的前缀
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web:";
    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PREFIX + "token:admin:";
    public static final String REDIS_KEY_CATEGORY_INFO = REDIS_KEY_PREFIX + "category:list:";
    public static final String TOKEN_WEB_COOKIE_NAME = "token";
    public static final String TOKEN_ADMIN_COOKIE_NAME = "adminToken";

    public static final String FILE_FOLDER = "file/";
    public static final String FILE_FOLDER_TEMP = "temp/";
    public static final String FILE_FOLDER_COVER = "cover/";    // 封面图片（缩略图）

    public static final String IMAGE_THUMBNAIL_SUFFIX = "_thumbnail.jpeg";   // 图片缩略图的后缀
}

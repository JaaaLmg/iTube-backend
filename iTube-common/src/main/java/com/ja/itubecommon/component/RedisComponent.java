package com.ja.itubecommon.component;

import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.dto.TokenUserInfoDto;
import com.ja.itubecommon.entity.po.CategoryInfo;
import com.ja.itubecommon.redis.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveVerifyCode(String code) {
        String verifyCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_VERIFY_CODE+verifyCodeKey, code, Constants.REDIS_KEY_EXPIRE_ONE_MIN*10);
        return verifyCodeKey;
    }

    public String getVerifyCode(String verifyCodeKey) {
        String code = (String) redisUtils.get(Constants.REDIS_KEY_VERIFY_CODE+verifyCodeKey);
        return code;
    }

    public void deleteVerifyCode(String verifyCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_VERIFY_CODE+verifyCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto) {
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRE_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, tokenUserInfoDto, Constants.REDIS_KEY_EXPIRE_ONE_DAY * 7);
    }

    public TokenUserInfoDto getTokenInfo(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public String getTokenInfo4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void deleteTokenInfo(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public void deleteTokenInfo4Admin(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public String saveTokenInfo4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token, account, Constants.REDIS_KEY_EXPIRE_ONE_DAY);
        return token;
    }

    public void saveCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_INFO, categoryInfoList);
    }
}

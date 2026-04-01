package com.ja.itubecommon.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Token用户信息 DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TokenUserInfoDto implements Serializable {
    private String userId;
    private String nickname;
    private String avatar;
    private Long expireAt;
    private String token;

    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}

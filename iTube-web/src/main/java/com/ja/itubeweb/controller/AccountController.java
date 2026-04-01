package com.ja.itubeweb.controller;

import com.ja.itubecommon.component.RedisComponent;
import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.dto.TokenUserInfoDto;
import com.ja.itubecommon.entity.vo.ResponseVO;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("account")
@Validated
public class AccountController extends BaseController {
	@Resource
	private UserInfoService userInfoService;

	@Resource
	private RedisComponent redisComponent;

	@RequestMapping("verifyCode")
	public ResponseVO verifyCode() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);		// 生成验证码
		String code = captcha.text();	// 获取验证码结果
		String verifyCodeKey = redisComponent.saveVerifyCode(code);		// 保存验证码到redis
		String verifyCodeBase64 = captcha.toBase64();

		// 返回结果
		Map<String, String> result = new HashMap<>();
		result.put("verifyCodeKey", verifyCodeKey);
		result.put("verifyCodeBase64", verifyCodeBase64);
		return getSuccessResponseVO(result);
	}

	@RequestMapping("register")
	public ResponseVO register(@NotEmpty @Email @Size(max=150) String email,
							   @NotEmpty @Size(max=20) String nickname,
							   @NotEmpty @Pattern(regexp=Constants.REGEX_PASSWORD) String password,
							   @NotEmpty String verifyCodeKey,
							   @NotEmpty String verifyCode) throws BusinessException {
		try {
			if(!verifyCode.equalsIgnoreCase(redisComponent.getVerifyCode(verifyCodeKey))) {
				throw new BusinessException("验证码错误");
			}
			userInfoService.register(email, nickname, password);
			return getSuccessResponseVO(null);
		} finally {
			redisComponent.deleteVerifyCode(verifyCodeKey);
		}
	}

	@RequestMapping("login")
	public  ResponseVO login(HttpServletResponse response,
							 @NotEmpty @Email String email,
							 @NotEmpty String password,		// 前端传回的密码需要是MD5加密后的结果
							 @NotEmpty String verifyCodeKey,
							 @NotEmpty String verifyCode) throws BusinessException {
		try {
			if(!verifyCode.equalsIgnoreCase(redisComponent.getVerifyCode(verifyCodeKey))) {
				throw new BusinessException("验证码错误");
			}
			String ip = getIpAddr();
			TokenUserInfoDto tokenUserInfoDto = userInfoService.login(email, password, ip);
			saveToken2Cookie(response, tokenUserInfoDto.getToken());

			return getSuccessResponseVO(tokenUserInfoDto);
		} finally {
			redisComponent.deleteVerifyCode(verifyCodeKey);
		}
	}

	@RequestMapping("autoLogin")
	public ResponseVO autoLogin(HttpServletResponse response) {
		// 前端请求需要在Header中直接携带token字段！
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
		if(null == tokenUserInfoDto) {
			return getSuccessResponseVO(null);
		}
		if(tokenUserInfoDto.getExpireAt() - System.currentTimeMillis() < Constants.REDIS_KEY_EXPIRE_ONE_DAY) {
			redisComponent.saveTokenInfo(tokenUserInfoDto);
			saveToken2Cookie(response, tokenUserInfoDto.getToken());
		}
		return getSuccessResponseVO(tokenUserInfoDto);
	}

	@RequestMapping("logout")
	public ResponseVO logout(HttpServletResponse response) {
		cleanTokenFromCookie(response);
		return getSuccessResponseVO(null);
	}
}
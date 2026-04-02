package com.ja.itubeadmin.controller;

import com.ja.itubecommon.component.RedisComponent;
import com.ja.itubecommon.entity.config.AppConfig;
import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.dto.TokenUserInfoDto;
import com.ja.itubecommon.entity.vo.ResponseVO;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.service.UserInfoService;
import com.ja.itubecommon.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
	private AppConfig appConfig;

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

	@RequestMapping("login")
	public  ResponseVO login(HttpServletResponse response,
							 @NotEmpty String account,
							 @NotEmpty String password,		// 前端传回的密码需要是MD5加密后的结果
							 @NotEmpty String verifyCodeKey,
							 @NotEmpty String verifyCode) throws BusinessException {
		try {
			if(!verifyCode.equalsIgnoreCase(redisComponent.getVerifyCode(verifyCodeKey))) {
				throw new BusinessException("验证码错误");
			}
			if(!account.equals(appConfig.getAdminAccount()) || !password.equals((StringTools.encodeByMd5(appConfig.getAdminPassword())))) {
				throw new BusinessException("账号或密码错误");
			}
			String token = redisComponent.saveTokenInfo4Admin(account);
			saveToken2Cookie(response, token);
			return getSuccessResponseVO(account);
		} finally {
			redisComponent.deleteVerifyCode(verifyCodeKey);
		}
	}

	@RequestMapping("logout")
	public ResponseVO logout(HttpServletResponse response) {
		cleanTokenFromCookie(response);
		return getSuccessResponseVO(null);
	}
}
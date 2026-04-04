package com.ja.itubeadmin.interceptor;

import com.ja.itubecommon.component.RedisComponent;
import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.enums.ResponseCodeEnum;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.utils.StringTools;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppInterceptor implements HandlerInterceptor {
    private final String URL_ACCOUNT = "/account";
    private final String URL_FILE = "/file";

    @Resource
    private RedisComponent redisComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) return true;
        if(request.getRequestURI().contains(URL_ACCOUNT)) return true;

        String token = request.getHeader(Constants.TOKEN_ADMIN_COOKIE_NAME);    // 一般情况下，前端必须在请求头中携带token
        if(request.getRequestURI().contains(URL_FILE)) {        // 涉及文件的请求，则从cookie中获取token
            token = getTokenFromCookie(request);
        }
        if(StringTools.isEmpty(token) || redisComponent.getTokenInfo4Admin(token) == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }

        return true;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(Constants.TOKEN_ADMIN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

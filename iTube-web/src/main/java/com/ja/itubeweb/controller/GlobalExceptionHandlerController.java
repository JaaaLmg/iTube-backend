package com.ja.itubeweb.controller;

import com.ja.itubecommon.entity.enums.ResponseCodeEnum;
import com.ja.itubecommon.entity.vo.ResponseVO;
import com.ja.itubecommon.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址: {} ， 错误信息: {}", request.getRequestURL(), e.toString());
        ResponseVO ajaxResponse = new ResponseVO();

        if(e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        else if(e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            ajaxResponse.setCode(businessException.getCode() == null ? ResponseCodeEnum.CODE_600.getCode() : businessException.getCode());
            ajaxResponse.setInfo(businessException.getMessage());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        else if(e instanceof BindException || e instanceof ConstraintViolationException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        else if(e instanceof DuplicateKeyException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_601.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        else {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        return ajaxResponse;
    }
}

package com.ja.itubecommon.exception;
import com.ja.itubecommon.entity.enums.ResponseCodeEnum;
public class BusinessException extends Exception{
    private ResponseCodeEnum codeEnum;

    private Integer code;

    private String message;

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public ResponseCodeEnum getCodeEnum() {
        return codeEnum;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 重写fillInStackTrace方法，不填充堆栈信息，减少无用的数据
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

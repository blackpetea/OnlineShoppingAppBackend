package com.zhensmallgroup.mall.exception;

public class ZhensMallException extends RuntimeException{
    private final Integer code;
    private final String message;

    public ZhensMallException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public ZhensMallException(ZhensMallExceptionEnum exceptionEnum){
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());

    }
    public Integer getCode(){
        return code;
    }
}

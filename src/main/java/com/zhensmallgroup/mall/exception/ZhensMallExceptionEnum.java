package com.zhensmallgroup.mall.exception;



public enum ZhensMallExceptionEnum {

    NEED_USER_NAME(10001, "Username Can't Be Empty"),
    NEED_PASSWORD(10002, "Password can't be empty"),
    PASSWORD_TOO_SHORT(10003, "Password too short"),

    NAME_ALREADY_EXISTS(10004, "name already Exists"),

    INSERT_FAILED(10005, "Insert Failed, Please try again"),

    SYSTEM_ERROR(20000, "system malfunctioned"),

    WRONG_PASSWORD(10006, "login failed, password is wrong"),

    NEED_LOGIN(10007, "User is not logged in"),

    UPDATE_FAILED(10008, "User Log in failed"),

    PARAM_NOT_NULL(10010, "Parameter can't be null"),

    CREATE_FAILED(10011, "create failed"),

    REQUEST_PARAM_ERROR(10012, "wrong parameters"),

    NEED_ADMIN(10009, "not a admin");
    Integer code;
    String msg;

    ZhensMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

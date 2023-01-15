package com.zhensmallgroup.mall.common;

import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;

public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    // general type for data, since it could be any type
    private T data;
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //no args needs to pass back means success request,
    public ApiRestResponse(){
        //calling the 2 args constructor
        this(OK_CODE, OK_MSG);

    }

    //will be called when success
    public static <T> ApiRestResponse<T> success(){
        //calling the no args constructor
        return new ApiRestResponse<>();
    }

    //has args for passing data as result
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    //when failed
    public static <T> ApiRestResponse<T> error(Integer code, String msg){
        //call the 2 args constructor
        return new ApiRestResponse<>(code, msg);
    }

    public static <T> ApiRestResponse<T> error(ZhensMallExceptionEnum ex){
        //call the 2 args constructor
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

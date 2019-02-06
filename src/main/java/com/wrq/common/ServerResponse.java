package com.wrq.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wrq.enums.ResponseCode;


/**
 * Created by wangqian on 2019/1/28.
 */
public class ServerResponse {

    private int status;
    private String msg;
    private Object data;


    // 私有构造函数
    private ServerResponse(int status) {
        this.status = status;
    }

    // 私有构造函数
    private ServerResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }


    // 私有构造函数
    private ServerResponse(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 私有构造函数
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }


    // 使之不在json序列化结果当中
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    // 正确返回 200
    public static ServerResponse createBySuccess() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static ServerResponse createBySuccess(Object data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }

    public static ServerResponse createBySuccess(String msg, Object data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static ServerResponse createByError() {
        return new ServerResponse(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static ServerResponse createByError(String errorMessage) {
        return new ServerResponse(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static ServerResponse createByError(int errorCode, String errorMessage) {
        return new ServerResponse(errorCode, errorMessage);
    }

    public static ServerResponse createByError(ResponseCode responseCode) {
        return new ServerResponse(responseCode.getCode(), responseCode.getDesc());
    }

}

package com.example.springcloud.gateway.response;

/**
 * @author LDL
 * @description
 * @date 2019/11/10/0010 20:10
 */
public class ResponseData {

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
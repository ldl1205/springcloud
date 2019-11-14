package com.example.springcloud.gateway.retalimit;

import java.io.Serializable;

/**
 * @author LDL
 * @description
 * @date 2019/11/13/0013 18:24
 */
public class LimitKey implements Serializable {
    private static final long serialVersionUID = 443665495361171063L;
    private String api;
    private String biz;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }
}

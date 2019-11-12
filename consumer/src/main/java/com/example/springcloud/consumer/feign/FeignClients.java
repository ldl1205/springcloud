package com.example.springcloud.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author LDL
 * @Date: 2019/10/27/0027 18:11
 * @Description:
 */
@FeignClient(name = "produce")
public interface FeignClients {

    @RequestMapping(value = "/test/get",method = RequestMethod.GET)
    public String get();

}

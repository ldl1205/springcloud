package com.example.springcloud.gateway.fallback;

import com.example.springcloud.gateway.response.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LDL
 * @description 降级
 * @date 2019/11/14/0014 15:32
 */
@RestController
public class FallbackController {
    @GetMapping("/fallback")
    public ResponseData fallback() {
        ResponseData response = new ResponseData();
        response.setCode("100");
        response.setMsg("服务暂时不可用");
        return response;
    }
}

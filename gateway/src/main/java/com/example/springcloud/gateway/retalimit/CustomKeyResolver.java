package com.example.springcloud.gateway.retalimit;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LDL
 * @description
 * @date 2019/11/13/0013 13:53
 */
public class CustomKeyResolver implements KeyResolver {

    public static final String BEAN_NAME = "customKeyResolver";

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(getKey(exchange));
    }

    /**
     *
     * @param exchange
     * @return
     */
    private String getKey(ServerWebExchange exchange) {

        LimitKey limitKey = new LimitKey();

        limitKey.setApi(exchange.getRequest().getPath().toString());
        limitKey.setBiz(exchange.getRequest().getQueryParams().getFirst("biz"));

        return JSON.toJSONString(limitKey);
    }
}

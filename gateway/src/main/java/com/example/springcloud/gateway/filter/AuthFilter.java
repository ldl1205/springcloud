package com.example.springcloud.gateway.filter;

import com.example.springcloud.gateway.response.ResponseData;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author LDL
 * @description 认证过滤器
 * @date 2019/11/10/0010 20:05
 */
@Component
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if("token".equals(token)){
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        ResponseData data = new ResponseData();
        data.setCode("401");
        data.setMsg("非法请求");
//        JSON.toJSONString();
        return null;
    }
}

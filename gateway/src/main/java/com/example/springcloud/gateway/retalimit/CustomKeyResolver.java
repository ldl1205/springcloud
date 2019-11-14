package com.example.springcloud.gateway.retalimit;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LDL
 * @description 限流必须要有一个key，根据什么来进行限流，ip，接口，或者用户来进行限流，所以我们自定义一个KeyResolver
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

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RedisRateLimiter is not initialized");
        }

        LimitConfig limitConfig = getLimitConfig(routeId);

        if (limitConfig == null || limitConfig.getTokenConfig().size()==0) {
            return Mono.just(new Response(true,null));
        }

        Map<String, Config> conf = limitConfig.getTokenConfig();

        LimitKey limitKey = JSON.parseObject(id, LimitKey.class);
        //api限流
        String api = limitKey.getApi();
        Config apiConf = conf.get(api);
        //业务方限流
        String biz = limitKey.getBiz();
        Config bizConf = conf.get(biz);

        if (apiConf!=null) {
            return isSingleAllow(api,routeId,apiConf).flatMap(res -> {
                if (res.isAllowed()) {
                    if(bizConf!=null) {
                        return isSingleAllow(biz, routeId, bizConf);
                    }else {
                        return Mono.just(new Response(true,new HashMap<>()));
                    }
                }else {
                    return Mono.just(res);
                }
            } );
        }else {
            if (bizConf!=null) {
                return isSingleAllow(biz, routeId, bizConf);
            }else {
                return Mono.just(new Response(true,new HashMap<>()));
            }
        }
    }

    /**
     * 单级限流
     * @param api
     * @param routeId
     * @param apiConf
     * @return
     */
    private Mono<Response> isSingleAllow(String key, String routeId, Config config) {
        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = config.getReplenishRate();

        // How much bursting do you want to allow?
        int burstCapacity = config.getBurstCapacity();

        try {
            List<String> keys = getKeys(routeId+"$"+key);

            // The arguments to the LUA script. time() returns unixtime in seconds.
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                    Instant.now().getEpochSecond() + "", "1");
            // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
            Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys, scriptArgs);
            // .log("redisratelimiter", Level.FINER);
            return flux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }) .map(results -> {
                        boolean allowed = results.get(0) == 1L;
                        Long tokensLeft = results.get(1);

                        Response response = new Response(allowed, getHeaders(config, tokensLeft));

                        if (log.isDebugEnabled()) {
                            log.debug("response: " + response);
                        }
                        return response;
                    });
        }
        catch (Exception e) {
            /*
             * We don't want a hard dependency on Redis to allow traffic. Make sure to set
             * an alert so you know if this is happening too much. Stripe's observed
             * failure rate is 0.01%.
             */
            log.error("Error determining if user allowed from redis", e);
        }
        return Mono.just(new Response(true, getHeaders(config, -1L)));
    }

    private LimitConfig getLimitConfig(String routeId) {
        Map<String, LimitConfig> map = new HashMap<>();
        LimitConfig limitConfig = new LimitConfig();
        limitConfig.setRouteId("rateLimit_route");
        Map<String, Config> tokenMap = new HashMap<>();
        Config apiConfig = new Config();
        apiConfig.setBurstCapacity(5);
        apiConfig.setReplenishRate(5);

        Config bizConfig = new Config();
        bizConfig.setBurstCapacity(1);
        bizConfig.setReplenishRate(1);

        tokenMap.put("/hello/rateLimit", apiConfig);
        tokenMap.put("jieyin", bizConfig);
        limitConfig.setTokenConfig(tokenMap);
        map.put("rateLimit_route", limitConfig);
        return limitConfig;
    }
}

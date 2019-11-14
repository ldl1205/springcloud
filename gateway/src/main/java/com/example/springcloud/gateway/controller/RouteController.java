package com.example.springcloud.gateway.controller;

import com.example.springcloud.gateway.bean.GatewayFilterDefinition;
import com.example.springcloud.gateway.bean.GatewayPredicateDefinition;
import com.example.springcloud.gateway.bean.GatewayRouteDefinition;
import com.example.springcloud.gateway.service.DynamicRouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LDL
 * @description 路由增删改controller
 * @date 2019/11/14/0014 15:36
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 增加路由
     * @param gwdefinition
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDefinition gwdefinition) {
        try {
            RouteDefinition definition = assembleRouteDefinition(gwdefinition);
            return this.dynamicRouteService.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succss";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        return this.dynamicRouteService.delete(id);
    }

    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = assembleRouteDefinition(gwdefinition);
        return this.dynamicRouteService.update(definition);
    }

    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        List<PredicateDefinition> pdList=new ArrayList<>();
        definition.setId(gwdefinition.getId());
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList=gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }

        List<GatewayFilterDefinition> gatewayFilterDefinitions = gwdefinition.getFilters();
        List<FilterDefinition> filterList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(gatewayFilterDefinitions)) {
            for (GatewayFilterDefinition gatewayFilterDefinition : gatewayFilterDefinitions) {
                FilterDefinition filterDefinition = new FilterDefinition();
                filterDefinition.setName(gatewayFilterDefinition.getName());
                filterDefinition.setArgs(gatewayFilterDefinition.getArgs());
                filterList.add(filterDefinition);
            }
        }
        definition.setPredicates(pdList);
        definition.setFilters(filterList);
        URI uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        definition.setUri(uri);
        return definition;
    }
}

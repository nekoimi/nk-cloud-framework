package com.nekoimi.nk.devtools.controller;

import com.nekoimi.nk.devtools.request.GenReq;
import com.nekoimi.nk.devtools.service.GenerateService;
import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.mybatis.service.DbInforService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * nekoimi  2021/12/14 15:31
 */
@RestController
@Api(tags = "代码生成器接口")
public class GeneratorController {
    @Autowired
    private DbInforService dbInforService;
    @Autowired
    private GenerateService generateService;

    @ApiOperation("获取代码生成器配置")
    @GetMapping("generator/env")
    public Mono<JsonResp> env() {
        Map<String, Object> map = new HashMap<>();
        map.put("database", dbInforService.name());
        map.put("schemas", dbInforService.schemas());
        return Mono.just(JsonResp.ok(map));
    }

    @ApiOperation("获取数据表列表")
    @GetMapping("generator/env/{schema}/tables")
    public Mono<JsonResp> tables(@PathVariable("schema") String schema) {
        Map<String, Object> map = new HashMap<>();
        map.put("tables", dbInforService.tables(schema));
        return Mono.just(JsonResp.ok(map));
    }

    @ApiOperation("生成代码")
    @PostMapping("generator/generate")
    public Mono<JsonResp> generate(GenReq req) {
        generateService.generate(req);
        return Mono.just(JsonResp.ok());
    }
}

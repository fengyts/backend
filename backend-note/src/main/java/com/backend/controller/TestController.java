package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.config.mulitidb.MulitiDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MulitiDataSourceProperties mulitiDbs;
//    @Autowired
//    private MulitiDataSourceConfig c;

    @GetMapping("/mulitiCfg")
    public ResultData mulitiCfgTest(){
        String defaultDb = mulitiDbs.getDefaultDb();
//        Map<String, DataSource> dsList = c.getDataSourceList();
        return ResultData.ok(defaultDb);
    }

}

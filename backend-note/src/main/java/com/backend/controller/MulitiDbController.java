package com.backend.controller;

import com.backend.service.IMulitiDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mulitiDb")
public class MulitiDbController {

    //https://my.oschina.net/u/3681868/blog/1813011
    //https://www.cnblogs.com/aizen-sousuke/p/11756279.html

    @Autowired
    private IMulitiDbService mulitiDbService;

    @GetMapping("write")
    public Object db1(Long id){
        return mulitiDbService.testWrite(id);
    }

    @GetMapping("read")
    public Object db2(Long id){
        return mulitiDbService.testRead(id);
    }

    @GetMapping("other")
    public Object db3(Long id){
        return mulitiDbService.testOther(id);
    }

}

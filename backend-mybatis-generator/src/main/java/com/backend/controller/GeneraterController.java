package com.backend.controller;

import com.backend.config.GeneraterConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
@RequestMapping("/generate/")
public class GeneraterController {

    @GetMapping("initConfig")
    @ResponseBody
    public Object initConfig() {
        return null;
    }

    @PostMapping("generate")
    @ResponseBody
    public Object generate(GeneraterConfiguration config) {
        return null;
    }

    @PutMapping
    public Object clearCacheConfig() {
        return null;
    }


}

package com.backend.business;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/")
public class TestController {

    @RequestMapping("/view1")
    public String test1() {
        return "/views/view1";
    }

    @RequestMapping("/view2")
    public String test2() {
        return "/views/view2";
    }


}

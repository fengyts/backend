package com.backend.controller;

import com.backend.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

    @RequestMapping({"/","index"})
    public String index(){
        return "redirect:/holiday/index";
    }

}

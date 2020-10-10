package com.backend.controller;

import com.backend.common.BaseController;
import com.backend.util.SysUserHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

//    @RequestMapping({"/","index"})
    @RequestMapping({"/index"})
    public String index(Model model){
        setCurrentUser(model);
//        return "redirect:/holiday/index";
        return "/layuiAdmin";
    }

}

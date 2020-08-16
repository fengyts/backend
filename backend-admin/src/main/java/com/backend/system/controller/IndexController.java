package com.backend.system.controller;

import com.backend.common.BaseController;
import com.backend.system.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.Subject;

@Controller
public class IndexController extends BaseController {

    @RequestMapping("/login")
    public String login() {
        return "/common/index";
    }

    @RequestMapping({"/", "/index/"})
    public String index(Model model) {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject();
        model.addAttribute("userInfo", sysUser);
        return "/index/index";
    }

    @RequestMapping("/home")
    public String home(){
        return "/index/home";
    }


}

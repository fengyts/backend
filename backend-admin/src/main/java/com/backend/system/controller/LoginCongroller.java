package com.backend.system.controller;

import com.backend.common.AjaxResult;
import com.backend.common.BaseController;
import com.backend.system.config.propertie.SysProperties;
import com.backend.system.dto.LoginDto;
import com.backend.system.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginCongroller extends BaseController {

    @Autowired
    private SysProperties sysProp;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("kaptchaSwitch", sysProp.isKaptchaOpen());
        model.addAttribute("smsSwitch", sysProp.isSmsOpen());
        return "/index/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        return "/index/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult login(@RequestBody LoginDto loginDto) {
        logger.info("login user = " + loginDto);
        UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getUserName(), loginDto.getPasswd());
        //获取Subject 对象
        Subject subject = SecurityUtils.getSubject();
        try {
            if (loginDto.getRememberMe()) {
                token.setRememberMe(true);
            }
            subject.login(token);
            return AjaxResult.success("/index");
        } catch (UnknownAccountException e) {
            logger.info("用户名不存在：{}", e);
            return AjaxResult.error("用户名不存在");
        } catch (IncorrectCredentialsException e) {
            logger.info("用户名密码错误：{}", e);
            return AjaxResult.error("用户名密码错误");
        }
    }

//    @GetMapping("/403")
//    public String forbid() {
//        return "403";
//    }

    /*@GetMapping("/logout")
    public String logout(){
        return "redirect: /login";
    }*/



}

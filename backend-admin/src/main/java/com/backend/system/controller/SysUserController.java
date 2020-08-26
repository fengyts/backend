package com.backend.system.controller;

import com.backend.common.BaseController;
import com.backend.common.Constants;
import com.backend.common.ResultData;
import com.backend.common.WebPageQuery;
import com.backend.system.dto.SysUserDto;
import com.backend.system.entity.SysUser;
import com.backend.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/system/sysUser/")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping("list")
    public String list(){
        return "/system/sysUser/list";
    }

    @RequestMapping("queryPageList")
    @ResponseBody
    public Page<SysUserDto> queryPageList(Page<SysUserDto> webPageQuery){
        Page<SysUserDto> page = sysUserService.queryPageList(webPageQuery);
        return page;
    }

    @RequestMapping("add")
    public String add(Model model){
        model.addAttribute("initialPwd", Constants.SYS_USER_INITIAL_PWD);
        return "/system/sysUser/form";
    }

    @RequestMapping("edit")
    public String add(Long userId, Model model){
        SysUserDto sysUserDto = sysUserService.selectOneByUserId(userId);
        model.addAttribute("sysUser", sysUserDto);
        return "/system/sysUser/form";
    }

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public ResultData saveOrUpdate(@RequestBody SysUserDto dto){
        ResultData res = sysUserService.saveOrUpdate(dto);
        return res;
    }

}

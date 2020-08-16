package com.backend.system.controller;

import com.backend.common.BaseController;
import com.backend.system.entity.SysUser;
import com.backend.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public Page<SysUser> queryPageList(Page<SysUser> webPageQuery){
        Page<SysUser> page = sysUserService.queryPageList(webPageQuery);
        return page;
    }

}

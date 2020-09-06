package com.backend.system.controller;

import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/system/sysRole/")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService sysRoleService;

    @RequestMapping("list")
    public String list(){
        return "/system/sysRole/list";
    }

    @GetMapping("getAllRoles")
    @ResponseBody
    public ResultData getAllRoles(){
        List<SysRoleDto> roles = sysRoleService.listRoles();
        return ResultData.ok(roles);
    }

    @RequestMapping("add")
    public String add(){
        return "/system/sysRole/form";
    }

    @RequestMapping("edit")
    public String edit(Model model, Long id){
        SysRoleDto sysRoleDto = sysRoleService.selectById(id);
        model.addAttribute("sysRole", sysRoleDto);
        return "/system/sysRole/form";
    }

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public ResultData saveOrUpdate(@RequestBody SysRoleDto dto){
        ResultData res = sysRoleService.saveOrUpdate(dto);
        return res;
    }


}

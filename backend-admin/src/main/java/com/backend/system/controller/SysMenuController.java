package com.backend.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.enums.SysMenuTypeEnum;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.service.ISysMenuService;
import com.backend.system.service.ISysUserService;
import com.backend.system.shiro.ShiroUtil;
import com.backend.system.shiro.SysUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/system/sysMenu/")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService sysMenuService;

    @RequestMapping("list")
    public String listPage(Model model){
        model.addAttribute("menuTypes", SysMenuTypeEnum.values());
        return "system/sysMenu/list";
    }

    @GetMapping("getMenuList")
    @ResponseBody
    public JSONArray getMenuList(){
        Long id = SysUserUtils.getCurrentUser().getId();
        List<SysMenuDto> allMenus = sysMenuService.getAllMenus(id);
        JSONArray arrs = JSONArray.parseArray(JSONObject.toJSONString(allMenus));
        return arrs;
    }

    @RequestMapping("add")
    public String add(Model model, Long id){
        model.addAttribute("id", id);
        model.addAttribute("menuTypes", SysMenuTypeEnum.values());
        return "system/sysMenu/form";
    }

    @RequestMapping("edit")
    public String edit(Model model, Long id){
        SysMenuDto dto = sysMenuService.getMenuById(id);
        model.addAttribute("menuTypes", SysMenuTypeEnum.values());
        model.addAttribute("sysMenu", dto);
        return "system/sysMenu/form";
    }

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public ResultData saveOrUpdate(@RequestBody SysMenuDto dto){
        ResultData resultData = sysMenuService.saveOrUpdate(dto);
        return resultData;
    }

    @PostMapping("update")
    @ResponseBody
    public ResultData update(SysMenuDto dto){
        sysMenuService.updateSysMenu(dto);
        return ResultData.ok();
    }

}

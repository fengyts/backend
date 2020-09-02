package com.backend.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.backend.common.ResultData;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.service.ISysMenuService;
import com.backend.system.service.ISysUserService;
import com.backend.system.shiro.ShiroUtil;
import com.backend.system.shiro.SysUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/system/sysMenu/")
public class SysMenuController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysMenuService sysMenuService;

    @RequestMapping("list")
    public String listPage(){
        return "system/sysMenu/list";
    }

    @GetMapping("getMenuList")
    @ResponseBody
    public JSONArray getMenuList(){
        String arrs1 = "[{\"id\":1,\"pid\":0,\"title\":\"1-11\"},{\"id\":2,\"pid\":0,\"title\":\"1-2\"},{\"id\":3,\"pid\":0,\"title\":\"1-3\"},{\"id\":4,\"pid\":1,\"title\":\"1-1-1\"},{\"id\":5,\"pid\":1,\"title\":\"1-1-2\"},{\"id\":6,\"pid\":2,\"title\":\"1-2-1\"},{\"id\":7,\"pid\":2,\"title\":\"1-2-3\"},{\"id\":8,\"pid\":3,\"title\":\"1-3-1\"},{\"id\":9,\"pid\":3,\"title\":\"1-3-2\"},{\"id\":10,\"pid\":4,\"title\":\"1-1-1-1\"},{\"id\":11,\"pid\":4,\"title\":\"1-1-1-2\"}]";
        JSONArray jsonArray = JSONArray.parseArray(arrs1);
        return jsonArray;
    }

    @GetMapping("getMenuList1")
    @ResponseBody
    public JSONArray getMenuList1(){
        Long id = SysUserUtils.getCurrentUser().getId();
        List<SysMenuDto> allMenus = sysMenuService.getAllMenus(id);
        JSONArray arrs = JSONArray.parseArray(JSONObject.toJSONString(allMenus));
        return arrs;
    }

}

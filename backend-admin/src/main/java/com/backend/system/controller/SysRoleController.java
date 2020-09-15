package com.backend.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.dto.SysRoleMenuDto;
import com.backend.system.service.ISysMenuService;
import com.backend.system.service.ISysRoleService;
import com.backend.system.shiro.SysUserUtils;
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
    public String list() {
        return "/system/sysRole/list";
    }

    @GetMapping("getAllRoles")
    @ResponseBody
    public ResultData getAllRoles() {
        List<SysRoleDto> roles = sysRoleService.listRoles();
        return ResultData.ok(roles);
    }

    @RequestMapping("add")
    public String add() {
        return "/system/sysRole/form";
    }

    @RequestMapping("edit")
    public String edit(Model model, Long id) {
        SysRoleDto sysRoleDto = sysRoleService.selectById(id);
        model.addAttribute("sysRole", sysRoleDto);
        return "/system/sysRole/form";
    }

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public ResultData saveOrUpdate(@RequestBody SysRoleDto dto) {
        ResultData res = sysRoleService.saveOrUpdate(dto);
        return res;
    }

    @GetMapping("getAllMenus")
    @ResponseBody
    public ResultData getAllMenus(Long roleId) {
//        List<SysRoleMenuDto> roleMenus = sysRoleService.getSysRoleMenus(roleId);
//        ResultData res = ResultData.ok(roleMenus);
//        return res;
        String data = "[{\"label\":\"安徽省\",\"id\":\"001\",\"isOpen\":true,\"children\":[{\"label\":\"淮北市\",\"id\":\"001001\"},{\"label\":\"马鞍山市\",\"id\":\"001002\",\"isOpen\":true,\"children\":[{\"label\":\"花山区\",\"id\":\"001002001\",\"isOpen\":true,\"children\":[{\"label\":\"霍里街道\",\"disabled\":true,\"checked\":true,\"id\":\"001002002001\",\"radioDisabled\":true},{\"label\":\"桃源路\",\"id\":\"001002002002\"},{\"label\":\"湖东路\",\"checked\":true,\"id\":\"001002002003\",\"radioChecked\":true}]},{\"label\":\"雨山区\",\"id\":\"001002002\"},{\"label\":\"和县\",\"id\":\"001002003\"}]},{\"label\":\"合肥市\",\"id\":\"001003\"}]},{\"label\":\"河南省\",\"id\":\"002\",\"children\":[{\"label\":\"郑州市\",\"id\":\"002001\"},{\"label\":\"开封市\",\"id\":\"002002\"}]},{\"label\":\"江苏省\",\"id\":\"003\",\"children\":[{\"label\":\"苏州市\",\"id\":\"003001\"},{\"label\":\"南京市\",\"id\":\"003002\"},{\"label\":\"无锡市\",\"id\":\"003003\"},{\"label\":\"徐州市\",\"id\":\"003004\"}]}]";
        String data1 = "[{\"label\":\"安徽省\",\"id\":\"001\",\"isOpen\":true},{\"label\":\"安徽省1\",\"id\":\"002\",\"isOpen\":true}]";
        JSONArray array = JSONArray.parseArray(data);
        ResultData res = ResultData.ok(array);
        return res;
    }


    @PostMapping("associateRoleMenu")
    @ResponseBody
    public ResultData associateRoleMenu(Long roleId, List<String> menus) {
        ResultData res = sysRoleService.associateRoleMenu(roleId, menus);
        return res;
    }


}

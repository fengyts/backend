package com.backend.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.dto.SysRoleMenuDto;
import com.backend.system.service.ISysRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<SysRoleMenuDto> roleMenus = sysRoleService.getSysRoleMenus(roleId);
        ResultData res = ResultData.ok(roleMenus);
        return res;

//        String data = "[{\"label\":\"安徽省\",\"id\":\"001\",\"isOpen\":true,\"children\":[{\"label\":\"淮北市\",\"id\":\"001001\"},{\"label\":\"马鞍山市\",\"id\":\"001002\",\"isOpen\":true,\"children\":[{\"label\":\"花山区\",\"id\":\"001002001\",\"isOpen\":true,\"children\":[{\"label\":\"霍里街道\",\"disabled\":true,\"checked\":true,\"id\":\"001002002001\",\"radioDisabled\":true},{\"label\":\"桃源路\",\"id\":\"001002002002\"},{\"label\":\"湖东路\",\"checked\":true,\"id\":\"001002002003\",\"radioChecked\":true}]},{\"label\":\"雨山区\",\"id\":\"001002002\"},{\"label\":\"和县\",\"id\":\"001002003\"}]},{\"label\":\"合肥市\",\"id\":\"001003\"}]},{\"label\":\"河南省\",\"id\":\"002\",\"children\":[{\"label\":\"郑州市\",\"id\":\"002001\"},{\"label\":\"开封市\",\"id\":\"002002\"}]},{\"label\":\"江苏省\",\"id\":\"003\",\"children\":[{\"label\":\"苏州市\",\"id\":\"003001\"},{\"label\":\"南京市\",\"id\":\"003002\"},{\"label\":\"无锡市\",\"id\":\"003003\"},{\"label\":\"徐州市\",\"id\":\"003004\"}]}]";
//        String data1 = "[{\"label\":\"安徽省\",\"id\":\"001\",\"isOpen\":true},{\"label\":\"安徽省1\",\"id\":\"002\",\"isOpen\":true}]";
//        JSONArray array = JSONArray.parseArray(data);
//        ResultData res = ResultData.ok(array);
//        return res;

//        String data = "{\"code\":\"0\",\"data\":[{\"checked\":true,\"createUserId\":0,\"id\":1,\"isLeaf\":false,\"isShow\":1,\"level\":0,\"menuName\":\"系统管理\",\"modifyUserId\":14,\"parentId\":0,\"sort\":0,\"subRoleMenus\":[{\"checked\":true,\"createUserId\":0,\"id\":2,\"isLeaf\":false,\"isShow\":1,\"level\":1,\"menuName\":\"系统菜单\",\"modifyUserId\":0,\"parentId\":1,\"sort\":0,\"subRoleMenus\":[{\"checked\":true,\"createUserId\":0,\"id\":3,\"isLeaf\":false,\"isShow\":1,\"level\":2,\"menuName\":\"菜单管理\",\"modifyUserId\":0,\"parentId\":2,\"sort\":0,\"subRoleMenus\":[{\"checked\":false,\"createUserId\":0,\"id\":12,\"isLeaf\":true,\"isShow\":1,\"level\":9,\"menuName\":\"新增\",\"modifyUserId\":0,\"parentId\":3,\"sort\":0,\"subRoleMenus\":[],\"type\":2},{\"checked\":false,\"createUserId\":0,\"id\":13,\"isLeaf\":true,\"isShow\":1,\"level\":9,\"menuName\":\"添加\",\"modifyUserId\":0,\"parentId\":3,\"sort\":1,\"subRoleMenus\":[],\"type\":2},{\"checked\":false,\"createUserId\":0,\"id\":14,\"isLeaf\":true,\"isShow\":1,\"level\":9,\"menuName\":\"编辑\",\"modifyUserId\":0,\"parentId\":3,\"sort\":2,\"subRoleMenus\":[],\"type\":2}],\"type\":1,\"url\":\"/system/sysMenu/list\"},{\"checked\":true,\"createUserId\":0,\"id\":4,\"isLeaf\":true,\"isShow\":1,\"level\":2,\"menuName\":\"角色管理\",\"modifyUserId\":0,\"parentId\":2,\"sort\":1,\"subRoleMenus\":[],\"type\":1,\"url\":\"/system/sysRole/list\"},{\"checked\":true,\"createUserId\":0,\"id\":5,\"isLeaf\":true,\"isShow\":1,\"level\":2,\"menuName\":\"权限管理\",\"modifyUserId\":0,\"parentId\":2,\"sort\":2,\"subRoleMenus\":[],\"type\":1,\"url\":\"/system/sysRole/permissionList\"},{\"checked\":true,\"createUserId\":0,\"id\":6,\"isLeaf\":true,\"isShow\":1,\"level\":2,\"menuName\":\"用户管理\",\"modifyUserId\":0,\"parentId\":2,\"sort\":3,\"subRoleMenus\":[],\"type\":1,\"url\":\"/system/sysUser/list\"}],\"type\":1}],\"type\":0},{\"checked\":true,\"createUserId\":0,\"id\":7,\"isLeaf\":false,\"isShow\":1,\"level\":0,\"menuName\":\"应用管理\",\"modifyUserId\":0,\"parentId\":0,\"sort\":1,\"subRoleMenus\":[{\"checked\":true,\"createUserId\":0,\"id\":8,\"isLeaf\":false,\"isShow\":1,\"level\":1,\"menuName\":\"商品管理\",\"modifyUserId\":0,\"parentId\":7,\"sort\":0,\"subRoleMenus\":[{\"checked\":true,\"createUserId\":0,\"id\":9,\"isLeaf\":true,\"isShow\":1,\"level\":2,\"menuName\":\"商品列表\",\"modifyUserId\":0,\"parentId\":8,\"sort\":0,\"subRoleMenus\":[],\"type\":1,\"url\":\"/item/list\"}],\"type\":1},{\"checked\":false,\"createUserId\":0,\"id\":10,\"isLeaf\":false,\"isShow\":1,\"level\":1,\"menuName\":\"订单管理\",\"modifyUserId\":0,\"parentId\":7,\"sort\":0,\"subRoleMenus\":[{\"checked\":false,\"createUserId\":0,\"icon\":\"\",\"id\":11,\"isLeaf\":true,\"isShow\":1,\"level\":2,\"menuName\":\"订单列表\",\"modifyUserId\":14,\"parentId\":10,\"sort\":0,\"subRoleMenus\":[],\"type\":1,\"url\":\"/order/list\"}],\"type\":1}],\"type\":0}],\"jsonObject\":{\"msg\":\"SUCCESS\",\"code\":\"0\",\"data\":[{\"$ref\":\"$.data[0]\"},{\"$ref\":\"$.data[1]\"}]},\"msg\":\"SUCCESS\"}";
//        ResultData res = JSONObject.parseObject(data, ResultData.class);
//        return res;
    }


    @PostMapping("associateRoleMenu")
    @ResponseBody
    public ResultData associateRoleMenu(Long roleId, @RequestBody List<Long> menuIds) {
        ResultData res = sysRoleService.associateRoleMenu(roleId, menuIds);
        return res;
    }


}

package com.backend.system.controller;

import com.backend.common.BaseController;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.service.ISysMenuService;
import com.backend.system.shiro.SysUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/")
public class SysController extends BaseController {

    @Autowired
    private ISysMenuService sysMenuService;

    @GetMapping("/getMenus")
    public List<SysMenuDto> getMenus(){
        Long userId = SysUserUtils.getCurrentUser().getId();
        List<SysMenuDto> menus = sysMenuService.getAllMenusByTier(userId);
        return menus;
    }
}

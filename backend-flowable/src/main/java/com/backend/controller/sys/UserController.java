package com.backend.controller.sys;

import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.model.entity.SysUserEntity;
import com.backend.service.IUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DELL
 */
@Controller
@RequestMapping("/sysUser")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @GetMapping("/listAllUsers")
    @ResponseBody
    public ResultData getUserList(){
        List<SysUserEntity> users = userService.listAllUsers();
        return ResultData.ok(users);
    }

}

package com.backend.controller;

import com.backend.annotion.NonAutority;
import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.mock.SysUserMock;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.LoginForm;
import com.backend.util.SysUserHandler;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @GetMapping
    @NonAutority
    public String login() {
        return "/login/login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    @NonAutority
    public ResultData doLogin(@RequestBody LoginForm loginForm) {
        String userName = loginForm.getLoginName();
        String pwd = loginForm.getPasswd();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(pwd)) {
            return ResultData.err("用户名或密码不能为空");
        }
        SysUserMock mock = new SysUserMock();
        List<SysUserEntity> sysUsers = mock.getSysUsers();
        List<SysUserEntity> matchs = sysUsers.stream()
                .filter(u -> userName.equals(u.getLoginName()) && pwd.equals(u.getPasswd()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(matchs)) {
            SysUserEntity userEntity = matchs.get(0);
            SysUserHandler.userLogin(userEntity);
            return ResultData.ok("/index");
        } else {
            return ResultData.err("用户名或密码错误");
        }
    }


}

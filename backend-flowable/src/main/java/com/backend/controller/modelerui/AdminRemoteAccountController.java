package com.backend.controller.modelerui;

import com.backend.util.ModelerUISecurityUtil;
import com.google.common.collect.Lists;
import java.util.List;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.flowable.ui.common.security.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * flowable-modeler-ui 工作流设计器ui引擎自定义用户及权限配置
 * 该controller路径配置位置：scripts/configuration/url-config.js
 * @author fengyts
 */
@RestController
@RequestMapping("/app")
public class AdminRemoteAccountController {

    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getAccount() {
        // 初始化一个登陆用户
        ModelerUISecurityUtil.initAssumeUser();
        // 初始化一个用户
        UserRepresentation userRepresentation = ModelerUISecurityUtil.initUserRepresentation();
        // 初始化一个用户组
        List<GroupRepresentation> groups = ModelerUISecurityUtil.initGroups();
        userRepresentation.setGroups(groups);
        // 初始化用户权限
        List<String> privileges = ModelerUISecurityUtil.getPrivileges();
        userRepresentation.setPrivileges(privileges);

        return userRepresentation;
    }

}


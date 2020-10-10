package com.backend.controller.modelerui;

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

    // 设置一个密码
    private static final String PASSWORD = "Pw@123456";

    private static final String userId = "admin";
    private static final String displayName = "Administrator";
    private static final String firstName = "Super";
    private static final String lastName = "Admin";
    private static final String fullName = "SuperAdmin";
    private static final String tenantId = "flowable-modeler";
    private static final String email = "super_admin@flowable.org";

    private static final String groupId = "manager";
    private static final String groupName = "sysManager";
    private static final String groupType = "manager";


    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getAccount() {
        // 初始化一个登陆用户
        User assumeUser = initUser();
        SecurityUtils.assumeUser(assumeUser);
        // 初始化一个用户
        UserRepresentation userRepresentation = initUserRepresentation();
        // 初始化一个用户组
        List<GroupRepresentation> groups = initGroups();
        userRepresentation.setGroups(groups);
        // 初始化用户权限
        List<String> privileges = getPrivileges();
        userRepresentation.setPrivileges(privileges);

        return userRepresentation;
    }

    private static List<String> getPrivileges(){
        List<String> privileges = Lists.newArrayListWithCapacity(5);
        privileges.add(DefaultPrivileges.ACCESS_MODELER);
        privileges.add(DefaultPrivileges.ACCESS_IDM);
        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
        privileges.add(DefaultPrivileges.ACCESS_TASK);
        privileges.add(DefaultPrivileges.ACCESS_REST_API);
        return privileges;
    }

    private static List<GroupRepresentation> initGroups(){
        List<GroupRepresentation> groups = Lists.newArrayListWithCapacity(1);
        Group group = new RemoteGroup();
        group.setId(groupId);
        group.setName(groupName);
        group.setType(groupType);

        GroupRepresentation groupRepresentation = new GroupRepresentation(group);
        groups.add(groupRepresentation);

        return groups;
    }

    private static UserRepresentation initUserRepresentation(){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(email);
        userRepresentation.setFullName(fullName);
        userRepresentation.setTenantId(tenantId);
        return userRepresentation;
    }

    private static User initUser(){
        User user = new RemoteUser();
        user.setId(userId);
        user.setDisplayName(displayName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(PASSWORD);
        user.setTenantId(tenantId);
        return user;
    }

}


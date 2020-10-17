package com.backend.util;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.flowable.ui.common.security.SecurityUtils;

public class ModelerUISecurityUtil {

    // 设置一个密码
    private static final String PASSWORD = "Pw@123456";

    private static final String userId = "admin";
    private static final String displayName = "Administrator";
    private static final String firstName = "Super";
    private static final String lastName = "Admin";
    private static final String fullName = "SuperAdmin";
    private static final String tenantId = "flowable-modeler-ui";
    private static final String email = "super_admin@flowable.org";

    private static final String groupId = "manager";
    private static final String groupName = "sysManager";
    private static final String groupType = "manager";

    public static void initAssumeUser() {
        User assumeUser = null;
        try {
            assumeUser = SecurityUtils.getCurrentUserObject();
        } catch (Exception e) {
        }
        if (Objects.isNull(assumeUser)) {
            assumeUser = initUser();
            SecurityUtils.assumeUser(assumeUser);
        }
    }

    public static List<String> getPrivileges() {
        List<String> privileges = Lists.newArrayListWithCapacity(5);
        privileges.add(DefaultPrivileges.ACCESS_MODELER);
        privileges.add(DefaultPrivileges.ACCESS_IDM);
        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
        privileges.add(DefaultPrivileges.ACCESS_TASK);
        privileges.add(DefaultPrivileges.ACCESS_REST_API);
        return privileges;
    }

    public static List<GroupRepresentation> initGroups() {
        List<GroupRepresentation> groups = Lists.newArrayListWithCapacity(1);
        Group group = new RemoteGroup();
        group.setId(groupId);
        group.setName(groupName);
        group.setType(groupType);

        GroupRepresentation groupRepresentation = new GroupRepresentation(group);
        groups.add(groupRepresentation);

        return groups;
    }

    public static UserRepresentation initUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(email);
        userRepresentation.setFullName(fullName);
        userRepresentation.setTenantId(tenantId);
        return userRepresentation;
    }

    private static User initUser() {
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

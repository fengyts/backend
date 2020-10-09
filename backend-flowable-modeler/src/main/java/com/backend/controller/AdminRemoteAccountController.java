package com.backend.controller;

import java.util.ArrayList;
import java.util.List;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.flowable.ui.common.security.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class AdminRemoteAccountController {

    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getAccount() {
        User user = new UserEntityImpl();
        user.setId("admin");
        user.setDisplayName("Administrator");
        SecurityUtils.assumeUser(user);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("admin");
        userRepresentation.setFirstName("admin");
        List<String> privileges = new ArrayList<>();
        List<String> pris = new ArrayList<>();
        pris.add(DefaultPrivileges.ACCESS_MODELER);
        pris.add(DefaultPrivileges.ACCESS_IDM);
        pris.add(DefaultPrivileges.ACCESS_ADMIN);
        pris.add(DefaultPrivileges.ACCESS_TASK);
        pris.add(DefaultPrivileges.ACCESS_REST_API);
        userRepresentation.setPrivileges(privileges);
        return userRepresentation;
    }


//    @RequestMapping(value = "/rest/account", method = RequestMethod.GET, produces = "application/json")
//    public UserRepresentation getAccount() {
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setId("admin");
//        userRepresentation.setEmail("admin@flowable.org");
//        userRepresentation.setFullName("Test Administrator");
//        userRepresentation.setLastName("Administrator");
//        userRepresentation.setFirstName("Test");
//        List<String> privileges = new ArrayList<>();
//        privileges.add(DefaultPrivileges.ACCESS_MODELER);
//        privileges.add(DefaultPrivileges.ACCESS_IDM);
//        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
//        privileges.add(DefaultPrivileges.ACCESS_TASK);
//        privileges.add(DefaultPrivileges.ACCESS_REST_API);
//        userRepresentation.setPrivileges(privileges);
//        return userRepresentation;
//    }

    public static User getCurrentUserObject() {
//        if (assumeUser != null) {
//            return assumeUser;
//        }

        RemoteUser user = new RemoteUser();
        user.setId("admin");
        user.setDisplayName("admin");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@admin.com");
        user.setPassword("test");
        List<String> pris = new ArrayList<>();
        pris.add(DefaultPrivileges.ACCESS_MODELER);
        pris.add(DefaultPrivileges.ACCESS_IDM);
        pris.add(DefaultPrivileges.ACCESS_ADMIN);
        pris.add(DefaultPrivileges.ACCESS_TASK);
        pris.add(DefaultPrivileges.ACCESS_REST_API);
        user.setPrivileges(pris);
        return user;
    }

}


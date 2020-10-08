package com.backend.controller;

import com.backend.common.BaseController;
import com.backend.component.ProcessImgGenerator;
import com.backend.model.entity.SysUserEntity;
import com.backend.util.SysUserHandler;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class FlowableBaseController extends BaseController {

    @Qualifier("processEngine")
    @Autowired
    protected ProcessEngine processEngine;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected ManagementService managementService;
    @Autowired
    protected IdentityService identityService;

    @Autowired
    protected ProcessImgGenerator generator;

    /**
     * 判断是否挂起状态
     * @param processInstanceId 流程实例id
     * @return
     */
    public boolean isSuspended(String processInstanceId) {
        boolean flag = true;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null){
            flag = !processInstance.isSuspended();
        }
        return flag;
    }

}

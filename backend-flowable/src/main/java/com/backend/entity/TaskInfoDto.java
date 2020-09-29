package com.backend.entity;

import lombok.Data;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.DelegationState;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@Data
public class TaskInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private Integer priority;
    private String owner;
    private String assignee;
    private String processInstanceId;
    private String executionId;
    private String taskDefinitionId;
    private String processDefinitionId;
    private String scopeId;
    private String subScopeId;
    private String scopeType;
    private String scopeDefinitionId;
    private String propagatedStageInstanceId;
    private Date createTime;
    private String taskDefinitionKey;
    private Date dueDate;
    private String category;
    private String parentTaskId;
    private String tenantId;
    private String formKey;
    private Map<String, Object> taskLocalVariables;
    private Map<String, Object> processVariables;
    private List<? extends IdentityLinkInfo> identityLinks;
    private Date claimTime;
    private DelegationState delegationState;

}

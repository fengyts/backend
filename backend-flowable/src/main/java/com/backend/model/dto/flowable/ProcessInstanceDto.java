package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author DELL
 */
@Data
public class ProcessInstanceDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    private String processDefinitionId;
    private String processDefinitionName;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private String deploymentId;
    private String businessKey;
    private Boolean isSuspended;
    private String tenantId;
    private String name;
    private String description;
    private String localizedName;
    private String localizedDescription;
    private Date startTime;
    private String startUserId;
    private String callbackId;
    private String callbackType;

    private String id;
    private Boolean isEnded;
    private String activityId;
    private String processInstanceId;
    private String rootProcessInstanceId;
    private String referenceId;
    private String referenceType;
    private String propagatedStageInstanceId;

    private Map<String, Object> processVariables;

}

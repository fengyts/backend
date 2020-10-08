package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntity;

/**
 * @author fengyts
 */
@Data
public class HistoricProcessInstanceDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    protected String endActivityId;
    protected String businessKey;
    protected String startUserId;
    protected String startActivityId;
    protected String superProcessInstanceId;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String name;
    protected String localizedName;
    protected String description;
    protected String localizedDescription;
    protected String processDefinitionKey;
    protected String processDefinitionName;
    protected Integer processDefinitionVersion;
    protected String deploymentId;
    protected String callbackId;
    protected String callbackType;
    protected String referenceId;
    protected String referenceType;
    protected List<HistoricVariableInstanceEntity> queryVariables;

    protected String processInstanceId;
    protected String processDefinitionId;
    protected Date startTime;
    protected Date endTime;
    protected Long durationInMillis;
    protected String deleteReason;

}

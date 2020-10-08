package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import java.util.Date;
import lombok.Data;

@Data
public class HistoricActivityInstanceDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    private String id;
    private String activityId;
    private String activityName;
    private String activityType;
    private String processDefinitionId;
    private String processInstanceId;
    private String executionId;
    private String assignee;
    private String taskId;
    private String calledProcessInstanceId;
    private String tenantId;

    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    private String deleteReason;

    private Date time;

}

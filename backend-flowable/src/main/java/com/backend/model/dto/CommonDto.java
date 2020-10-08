package com.backend.model.dto;

import com.backend.enums.StatusAuditEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import lombok.Data;

/**
 * @author fengyts
 */
@Data
public class CommonDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 审核状态
     */
    private StatusAuditEnum statusAudit;
    private Map<String, String> statusPairValue = StatusAuditEnum.statusPairValue();
    /**
     * 驳回原因
     */
    private String rejectReason;

    private Map<String, Object> persistentState;

    private String id;
    private String name;

    private String deploymentId;
    private String processDefinitionId;
    private String processInstanceId;
    private String taskId;
    private String executionId;

    private String assignee;
    /**
     * Time when the task created.
     *
     * @deprecated use {@link #getCreateTime()} instead
     **/
    @Deprecated
    private Date startTime;
    private Date createTime;
    private Date endTime;

}

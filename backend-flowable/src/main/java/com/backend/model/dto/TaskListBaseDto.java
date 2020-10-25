package com.backend.model.dto;

import com.backend.enums.StatusAuditEnum;
import java.io.Serializable;
import lombok.Data;

@Data
public abstract class TaskListBaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private StatusAuditEnum[] statusValues = StatusAuditEnum.values();

    /** 状态 */
    private StatusAuditEnum status;

    private String statusDesc;

    /**
     * 当前节点名称
     */
    private String currentNode;

    /** 当前节点处理人 */
    private String currentNodeHandler;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务申请人
     */
    private String applyUser;

}

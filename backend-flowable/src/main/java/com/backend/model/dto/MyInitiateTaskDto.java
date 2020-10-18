package com.backend.model.dto;

import com.backend.enums.StatusAuditEnum;
import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import java.io.Serializable;
import lombok.Data;

@Data
public class MyInitiateTaskDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private StatusAuditEnum[] statusValues = StatusAuditEnum.values();

    private HistoricProcessInstanceDto myInitiate;

    /** 状态 */
    private StatusAuditEnum status;

    private String statusDesc;

    /** 当前节点 */
    private String currentNodeHandler;

}

package com.backend.model.dto;

import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import lombok.Data;

/**
 * @author fengyts
 */
@Data
public class MyInitiateTaskDto extends TaskListBaseDto{

    private static final long serialVersionUID = 1L;

    private HistoricProcessInstanceDto myInitiate;

}

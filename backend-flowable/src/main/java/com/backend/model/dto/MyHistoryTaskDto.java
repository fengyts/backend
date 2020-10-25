package com.backend.model.dto;

import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import lombok.Data;

/**
 * @author fengyts
 */
@Data
public class MyHistoryTaskDto extends TaskListBaseDto {

    private static final long serialVersionUID = 1L;

    private HistoricTaskInstanceDto historicTask;

}

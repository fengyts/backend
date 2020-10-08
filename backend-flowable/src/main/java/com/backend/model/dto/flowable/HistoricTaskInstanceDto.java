package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import java.util.Date;
import lombok.Data;

@Data
public class HistoricTaskInstanceDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    private TaskInfoDto taskInfo;

    private String deleteReason;
    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    private Long workTimeInMillis;
    private Date claimTime;

    private Date time;

}

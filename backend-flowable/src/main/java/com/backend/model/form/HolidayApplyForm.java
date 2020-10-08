package com.backend.model.form;

import com.backend.enums.HolidayTypeEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 请假申请 保存表单
 * @author fengyts
 */
@Data
public class HolidayApplyForm implements Serializable {
    private static final long serialVersionUID = 1L;

//    private Long userId;
//    private String userName;
    private HolidayTypeEnum holidayType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer day;
    private Long nextNodeUserId;

}

package com.backend.model.dto;

import com.backend.model.dto.flowable.TaskInfoDto;
import lombok.Data;

@Data
public class MyToDoTaskDto extends TaskListBaseDto{

    private static final long serialVersionUID = 1L;

    private TaskInfoDto taskInfo;

}

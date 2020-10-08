package com.backend.service;

import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import java.util.List;
import java.util.Map;

public interface FlowableService {

    List<DeploymentDto> listDeployment();

    List<ProcessDefinitionDto> listProcessDefinition();

    @Deprecated
    List<TaskInfoDto> listTask(String assignee);

    /**
     * 获取当前正在办理中的任务列表
     * @param assigne
     * @return
     */
    List<ActivityInstanceDto> listRuntimeTask(String assigne);

    ProcessInstanceDto startProcess(String processKey, Map<String, Object> variables);

    TaskInfoDto getTaskByProcessInstanceId(String processInstanceId);

    TaskInfoDto getTaskByAssignee(String assigne);

    void completeTask(String taskId);

    void completeTask(String taskId, Map<String, Object> variables);

    void completeTask(String taskId, String variableKey, Object variableValue);

    void addComment(String userId, String taskId, String processInstanceId, String type, String msg);

}

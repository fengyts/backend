package com.backend.service;

import com.backend.common.ResultData;
import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.HiCommentDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 开启流程任务
     * @param processKey
     * @param variables
     * @return
     */
    ProcessInstanceDto startProcess(String processKey, Map<String, Object> variables);

    TaskInfoDto getTaskByProcessInstanceId(String processInstanceId);

    TaskInfoDto getTaskByAssignee(String assigne);

    void completeTask(String taskId);

    void completeTask(String taskId, Map<String, Object> variables);

    void completeTask(String taskId, String variableKey, Object variableValue);

    /**
     * 添加任务备注信息
     * @param userId
     * @param taskId
     * @param processInstanceId
     * @param type
     * @param msg
     */
    void addComment(String userId, String taskId, String processInstanceId, String type, String msg);

    void addComment(String taskId, String type, String msg);

    void updateCommentById(HiCommentDto comment);

    /**
     * 导入流程模型
     * @param file
     * @return
     */
    ResultData importProcessModel(MultipartFile file);

    /**
     * 获取流程xml文件
     * @param modelId
     * @return
     */
    byte[] getModelBpmnXML(String modelId);

    /**
     * 流程是否存在
     * @param processKey 流程定义中的 process的id
     * @return
     */
    boolean existModel(String processKey);

}

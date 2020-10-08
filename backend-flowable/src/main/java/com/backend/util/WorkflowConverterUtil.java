package com.backend.util;


import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.model.dto.CommonDto;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.HistoricActivityInstanceDto;
import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

/**
 * @author DELL
 */
@Slf4j
public class WorkflowConverterUtil {

    public static <T> Map<String, Object> getPersistentState(T instance) {
        Map<String, Object> persistentState = null;
        if (instance instanceof Entity) {
            Entity entity = (Entity) instance;
            persistentState = (Map<String, Object>) entity.getPersistentState();
            return persistentState;
        }
        return null == persistentState ? Maps.newHashMapWithExpectedSize(0) : persistentState;
    }

    private static <T> void setPersistentState(T instance, CommonDto entityDto) {
        entityDto.setPersistentState(getPersistentState(instance));
    }

    public static DeploymentDto toDeploymentDto(Deployment deployment) {
        DeploymentDto dto = new DeploymentDto();
        try {
            setPersistentState(deployment, dto);

            dto.setParentDeploymentId(deployment.getParentDeploymentId());
            dto.setId(deployment.getId());
            dto.setName(deployment.getName());
            dto.setDeploymentTime(deployment.getDeploymentTime());
            dto.setCategory(deployment.getCategory());
            dto.setKey(deployment.getKey());
            dto.setDerivedFrom(deployment.getDerivedFrom());
            dto.setDerivedFromRoot(deployment.getDerivedFromRoot());
            dto.setTenantId(deployment.getTenantId());
            dto.setEngineVersion(deployment.getEngineVersion());
            dto.setIsNew(deployment.isNew());

            try {
                dto.setResources(deployment.getResources());
            } catch (Exception e) {
                // 这里异常不抛出，只打印日志
                log.info("转换实体方法-toDeploymentDto()-异常: getResources获取为空");
            }
        } catch (Exception e) {
            log.info("转换实体方法-toDeploymentDto()-异常: {}", e);
        }
        return dto;
    }

    public static ProcessDefinitionDto toProcessDefinitionDto(ProcessDefinition processDefinition) {
        ProcessDefinitionDto dto = new ProcessDefinitionDto();
        try {
            setPersistentState(processDefinition, dto);

            dto.setId(processDefinition.getId());
            dto.setDeploymentId(processDefinition.getDeploymentId());
            dto.setCategory(processDefinition.getCategory());
            dto.setName(processDefinition.getName());
            dto.setKey(processDefinition.getKey());
            dto.setDescription(processDefinition.getDescription());
            dto.setVersion(processDefinition.getVersion());
            dto.setResourceName(processDefinition.getResourceName());
            dto.setDiagramResourceName(processDefinition.getDiagramResourceName());
            dto.setHasStartFormKey(processDefinition.hasStartFormKey());
            dto.setHasGraphicalNotation(processDefinition.hasGraphicalNotation());
            dto.setIsSuspended(processDefinition.isSuspended());
            dto.setTenantId(processDefinition.getTenantId());
            dto.setDerivedFrom(processDefinition.getDerivedFrom());
            dto.setDerivedFromRoot(processDefinition.getDerivedFromRoot());
            dto.setDerivedVersion(processDefinition.getDerivedVersion());
            dto.setEngineVersion(processDefinition.getEngineVersion());
        } catch (Exception e) {
            log.info("转换实体方法-toProcessDefinitionDto()-异常: {}", e);
        }
        return dto;
    }

    public static ProcessInstanceDto toProcessInstanceDto(ProcessInstance processInstance) {
        ProcessInstanceDto dto = new ProcessInstanceDto();
        try {
            setPersistentState(processInstance, dto);

            dto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            dto.setProcessDefinitionName(processInstance.getProcessDefinitionName());
            dto.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            dto.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
            dto.setDeploymentId(processInstance.getDeploymentId());
            dto.setBusinessKey(processInstance.getBusinessKey());
            dto.setIsSuspended(processInstance.isSuspended());
            dto.setTenantId(processInstance.getTenantId());
            dto.setName(processInstance.getName());
            dto.setDescription(processInstance.getDescription());
            dto.setLocalizedName(processInstance.getLocalizedName());
            dto.setLocalizedDescription(processInstance.getLocalizedDescription());
            dto.setStartTime(processInstance.getStartTime());
            dto.setStartUserId(processInstance.getStartUserId());
            dto.setCallbackId(processInstance.getCallbackId());
            dto.setCallbackType(processInstance.getCallbackType());

            dto.setId(processInstance.getId());
            dto.setIsEnded(processInstance.isEnded());
            dto.setActivityId(processInstance.getActivityId());
            dto.setProcessInstanceId(processInstance.getProcessInstanceId());
            dto.setRootProcessInstanceId(processInstance.getRootProcessInstanceId());
            dto.setReferenceId(processInstance.getReferenceId());
            dto.setReferenceType(processInstance.getReferenceType());
            dto.setPropagatedStageInstanceId(processInstance.getPropagatedStageInstanceId());

            dto.setProcessVariables(processInstance.getProcessVariables());
        } catch (Exception e) {
            log.info("转换实体方法-toProcessInstanceDto()-异常: {}", e);
        }

        return dto;
    }

    public static TaskInfoDto toTaskDto(Task task) {
        TaskInfoDto dto = new TaskInfoDto();
        try {
            setPersistentState(task, dto);

            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setDescription(task.getDescription());
            dto.setPriority(task.getPriority());
            dto.setOwner(task.getOwner());
            dto.setAssignee(task.getAssignee());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setExecutionId(task.getExecutionId());
            dto.setTaskDefinitionId(task.getTaskDefinitionId());
            dto.setProcessDefinitionId(task.getProcessDefinitionId());
            dto.setScopeId(task.getScopeId());
            dto.setSubScopeId(task.getSubScopeId());
            dto.setScopeType(task.getSubScopeId());
            dto.setScopeDefinitionId(task.getScopeDefinitionId());
            dto.setPropagatedStageInstanceId(task.getPropagatedStageInstanceId());
            dto.setCreateTime(task.getCreateTime());
            dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
            dto.setDueDate(task.getDueDate());
            dto.setCategory(task.getCategory());
            dto.setParentTaskId(task.getParentTaskId());
            dto.setTenantId(task.getTenantId());
            dto.setFormKey(task.getFormKey());
            dto.setTaskLocalVariables(task.getTaskLocalVariables());
            dto.setProcessVariables(task.getProcessVariables());
            dto.setDelegationState(task.getDelegationState());

            try {
                dto.setIdentityLinks(task.getIdentityLinks());
            } catch (Exception e) {
                log.info("转换实体方法-toTaskDto()-异常: {}, getIdentityLinks()异常");
            }
        } catch (Exception e) {
            log.info("转换实体方法-toTaskDto()-异常: {}", e);
        }
        return dto;
    }

    public static TaskInfoDto toTaskInfoDto(TaskInfo taskInfo) {
        TaskInfoDto dto = new TaskInfoDto();
        try {
            if (taskInfo instanceof TaskInfo) {
                dto.setAssignee(taskInfo.getAssignee());
                dto.setCategory(taskInfo.getCategory());
                dto.setClaimTime(taskInfo.getClaimTime());
                dto.setCreateTime(taskInfo.getCreateTime());
                dto.setDescription(taskInfo.getDescription());
                dto.setDueDate(taskInfo.getDueDate());
                dto.setExecutionId(taskInfo.getExecutionId());
                dto.setFormKey(taskInfo.getFormKey());
                dto.setId(taskInfo.getId());

                dto.setName(taskInfo.getName());
                dto.setOwner(taskInfo.getOwner());
                dto.setParentTaskId(taskInfo.getParentTaskId());
                dto.setPriority(taskInfo.getPriority());
                dto.setProcessDefinitionId(taskInfo.getProcessDefinitionId());
                dto.setProcessInstanceId(taskInfo.getProcessInstanceId());
                dto.setProcessVariables(taskInfo.getProcessVariables());
                dto.setPropagatedStageInstanceId(taskInfo.getPropagatedStageInstanceId());
                dto.setScopeDefinitionId(taskInfo.getScopeDefinitionId());
                dto.setScopeId(taskInfo.getScopeId());
                dto.setScopeType(taskInfo.getScopeType());
                dto.setSubScopeId(taskInfo.getSubScopeId());

                try {
                    dto.setIdentityLinks(taskInfo.getIdentityLinks());
                } catch (Exception e) {
                    log.info("转换实体方法-toTaskInfoDto()-异常: getIdentityLinks()获取异常");
                }
            }
        } catch (Exception e) {
            log.info("转换实体方法-toTaskInfoDto()-异常: {}", e);
        }
        return dto;
    }

    private static TaskInfoDto toTaskInfoDto(HistoricTaskInstance instance) {
        TaskInfoDto dto = new TaskInfoDto();
        if (instance instanceof TaskInfo) {
            TaskInfo taskInfo = instance;
            dto = toTaskInfoDto(taskInfo);
        }
        return dto;
    }

    public static HistoricTaskInstanceDto toHistoricTaskInstanceDto(HistoricTaskInstance instance) {
        HistoricTaskInstanceDto dto = new HistoricTaskInstanceDto();
        try {
            setPersistentState(instance, dto);
            TaskInfoDto taskInfo = toTaskInfoDto(instance);
            BeanUtils.copyProperties(taskInfo, dto);
            dto.setTaskInfo(taskInfo);

            dto.setDeleteReason(instance.getDeleteReason());
            dto.setStartTime(instance.getStartTime());
            dto.setEndTime(instance.getEndTime());
            dto.setDurationInMillis(instance.getDurationInMillis());
            dto.setWorkTimeInMillis(instance.getWorkTimeInMillis());
            dto.setClaimTime(instance.getClaimTime());

            dto.setTime(instance.getTime());
        } catch (Exception e) {
            log.info("转换实体方法-toHistoricTaskInstanceDto()-异常: {}", e);
        }
        return dto;
    }

    public static HistoricActivityInstanceDto toHistoricActivityInstanceDto(HistoricActivityInstance instance) {
        HistoricActivityInstanceDto dto = new HistoricActivityInstanceDto();
        try {
            setPersistentState(instance, dto);

            dto.setId(instance.getId());
            dto.setActivityId(instance.getActivityId());
            dto.setActivityName(instance.getActivityName());
            dto.setActivityType(instance.getActivityType());
            dto.setAssignee(instance.getAssignee());
            dto.setCalledProcessInstanceId(instance.getCalledProcessInstanceId());
            dto.setDeleteReason(instance.getDeleteReason());
            dto.setDurationInMillis(instance.getDurationInMillis());
            dto.setEndTime(instance.getEndTime());
            dto.setExecutionId(instance.getExecutionId());
            dto.setProcessDefinitionId(instance.getProcessDefinitionId());
            dto.setProcessInstanceId(instance.getProcessInstanceId());
            dto.setStartTime(instance.getStartTime());
            dto.setCreateTime(instance.getStartTime());
            dto.setTaskId(instance.getTaskId());
            dto.setTenantId(instance.getTenantId());

            dto.setTime(instance.getTime());
        } catch (Exception e) {
            log.info("转换实体方法-toHistoricActivityInstanceDto()-异常: {}", e);
        }
        return dto;
    }

    public static ActivityInstanceDto toActivityInstanceDto(ActivityInstance instance){
        ActivityInstanceDto dto = new ActivityInstanceDto();
        try {
            setPersistentState(instance, dto);

            dto.setId(instance.getId());
            dto.setAssignee(instance.getAssignee());
            dto.setCreateTime(instance.getStartTime());
            dto.setStartTime(instance.getStartTime());
            dto.setActivityId(instance.getActivityId());
            dto.setActivityName(instance.getActivityName());
            dto.setActivityType(instance.getActivityType());
            dto.setCalledProcessInstanceId(instance.getCalledProcessInstanceId());
            dto.setDeleteReason(instance.getDeleteReason());
            dto.setDurationInMillis(instance.getDurationInMillis());
            dto.setEndTime(instance.getEndTime());
            dto.setExecutionId(instance.getExecutionId());
            dto.setProcessDefinitionId(instance.getProcessDefinitionId());
            dto.setProcessInstanceId(instance.getProcessInstanceId());
            dto.setTaskId(instance.getTaskId());
            dto.setTenantId(instance.getTenantId());

            dto.setTime(instance.getTime());
        } catch (Exception e) {
            log.info("转换实体方法-toActivityInstanceDto()-异常: {}", e);
        }
        return dto;
    }

    public static HistoricProcessInstanceDto toHistoricProcessInstanceDto(HistoricProcessInstance instance) {
        HistoricProcessInstanceDto dto = new HistoricProcessInstanceDto();
        try {
            setPersistentState(instance, dto);
            BeanUtils.copyProperties(instance, dto);
        } catch (BeansException e) {
            log.info("转换实体方法-toHistoricProcessInstanceDto()-异常: {}", e);
        }
        return dto;
    }
}

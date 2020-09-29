package com.backend.util;


import com.backend.entity.DeploymentDto;
import com.backend.entity.ProcessDefinitionDto;
import com.backend.entity.ProcessInstanceDto;
import com.backend.entity.TaskInfoDto;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.Map;

/**
 * @author DELL
 */
public class WorkflowConverterUtil {

    public static DeploymentDto toDeploymentDto(Deployment deployment) {
        DeploymentDto dto = new DeploymentDto();
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
        dto.setResources(deployment.getResources());
        return dto;
    }

    public static ProcessDefinitionDto toProcessDefinitionDto(ProcessDefinition processDefinition) {
        ProcessDefinitionDto dto = new ProcessDefinitionDto();
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

        return dto;
    }

    public static ProcessInstanceDto toProcessInstanceDto(ProcessInstance processInstance) {
        ProcessInstanceDto dto;

//        if(processInstance instanceof Entity){
//            Entity entity = (Entity) processInstance;
//            Map<String, Object> persistentState = (Map<String, Object>) entity.getPersistentState();
//            dto = BeanUtils.mapToBean(persistentState, ProcessInstanceDto.class);
//            return dto;
//        }

        dto = new ProcessInstanceDto();

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

        return dto;
    }

    public static TaskInfoDto toTaskDto(Task task) {
        TaskInfoDto dto;
//        if(task instanceof TaskEntityImpl){
        if(task instanceof Entity){
//            TaskEntityImpl entity = (TaskEntityImpl) task;
            Entity entity = (Entity) task;
            Map<String, Object> persistentState = (Map<String, Object>) entity.getPersistentState();
            dto = BeanUtils.mapToBean(persistentState, TaskInfoDto.class);
            return dto;
        }

        dto = new TaskInfoDto();

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

        dto.setIdentityLinks(task.getIdentityLinks());

        return dto;
    }

}

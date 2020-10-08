package com.backend.service.impl;

import com.backend.common.FlowableConstant;
import com.backend.enums.CommentTypeEnum;
import com.backend.enums.StatusAuditEnum;
import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.HiCommentDto;
import com.backend.model.dto.flowable.HistoricActivityInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.service.FlowableService;
import com.backend.util.SysUserHandler;
import com.backend.util.WorkflowConverterUtil;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cmd.AddCommentCmd;
import org.flowable.engine.impl.cmd.SaveCommentCmd;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.engine.task.Event;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowableServiceImpl implements FlowableService {

    @Autowired
    protected ProcessEngine processEngine;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;
//    @Autowired
//    protected DynamicBpmnService dynamicBpmnService;
    @Autowired
    protected ManagementService managementService;

    private String generateDefaultId(){
        String nextId = processEngine.getProcessEngineConfiguration().getIdGenerator().getNextId();
        return nextId;
    }

    @Override
    public void addComment(String userId, String taskId, String processInstanceId, String type, String msg) {
//        Command addCommentCmd = new AddCommentCmd(taskId, processInstanceId, type, msg);
        HiCommentDto comment = new HiCommentDto();
        comment.setId(generateDefaultId());
        comment.setUserId(userId);
        comment.setType(type);
        comment.setTaskId(taskId);
        comment.setProcessInstanceId(processInstanceId);
        comment.setAction(Event.ACTION_ADD_COMMENT);
        comment.setTime(new Date());
        comment.setMessage(msg);
        comment.setFullMessage(msg);
        Command commentUpdate = new SaveCommentCmd(comment);
//        managementService.executeCommand(commentUpdate);

//        Command commentCmdAdd = new AddCommentCmd(taskId, processInstanceId, type, msg);
//        managementService.executeCommand(commentCmdAdd);

        taskService.addComment(taskId, processInstanceId, type, msg);
    }

    @Override
    public List<DeploymentDto> listDeployment() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        List<DeploymentDto> result = deployments.stream().map(d -> {
            DeploymentDto dto = WorkflowConverterUtil.toDeploymentDto(d);
            return dto;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ProcessDefinitionDto> listProcessDefinition() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        List<ProcessDefinitionDto> result = processDefinitions.stream().map(processDefinition -> {
            ProcessDefinitionDto dto = WorkflowConverterUtil.toProcessDefinitionDto(processDefinition);
            return dto;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<TaskInfoDto> listTask(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskInfoDto> results = tasks.stream().map(t -> {
            TaskInfoDto dto = WorkflowConverterUtil.toTaskDto(t);
            return dto;
        }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<ActivityInstanceDto> listRuntimeTask(String assignee) {
        List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery().taskAssignee(assignee).list();
        final Map<String, Map<String, List<SequenceFlow>>> statusMap = Maps.newHashMap();
        List<ActivityInstanceDto> results = activityInstances.stream()
                .filter(d -> null != d.getDurationInMillis() || null != d.getEndTime())
                .map(ati -> {
                    ActivityInstanceDto dto = WorkflowConverterUtil.toActivityInstanceDto(ati);
                    setTaskStatus(dto, statusMap);
                    return dto;
                }).collect(Collectors.toList());
        return results;
    }

    private boolean isFinished(String taskId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskId(taskId).finished().list();
        return CollectionUtils.isEmpty(list);
    }

    /**
     * 设置当前任务的状态
     * 驳回时设置驳回原因
     *
     * @param dto
     * @param statusMap
     */
    private void setTaskStatus(ActivityInstanceDto dto, Map<String, Map<String, List<SequenceFlow>>> statusMap) {
        Long durationInMillis = dto.getDurationInMillis();
        Date endTime = dto.getEndTime();
        if (null == durationInMillis || null == endTime) {
            dto.setStatusAudit(StatusAuditEnum.UNDER_REVIEW);
        } else {
            // 获取当前活动节点的上一个sequenceFlow
            String activityType = BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW;
            List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery()
                    .activityType(activityType).processInstanceId(dto.getProcessInstanceId()).list();
            ActivityInstance previousSequenceFlow = activityInstances.get(activityInstances.size() - 1);
            // 获取当前节点的来源节点
            List<SequenceFlow> allIncomingFlows = getAllIncomingFlows(
                    dto.getExecutionId(),
                    dto.getProcessDefinitionId(),
                    FlowableConstant.SEQUENCE_FLOW_INCOMING,
                    statusMap);
            List<SequenceFlow> sequenceFlows = allIncomingFlows.stream().filter(f ->
                    f.getId().equals(previousSequenceFlow.getActivityId())
            ).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sequenceFlows)) {
                SequenceFlow sequenceFlow = sequenceFlows.get(0);
                dto.setStatusAudit(StatusAuditEnum.REJECTED);
                dto.setDeleteReason(sequenceFlow.getName());

                Map<String, Object> variables = runtimeService.getVariables(dto.getExecutionId());
//                Map<String, Object> variables = taskService.getVariables(dto.getTaskId());
                dto.setRejectReason((String) variables.get("rejectReason"));

            }
        }
    }

    private Map<String, Object> getVariables(String taskId, String variableName) {
        return null;
    }

    private List<SequenceFlow> getAllIncomingFlows(
            String executionId,
            String processDefinitionId,
            String sequenceType,
            Map<String, Map<String, List<SequenceFlow>>> statusMap) {
        Map<String, List<SequenceFlow>> eMaps = statusMap.get(executionId);
        if (null == eMaps) {
            List<SequenceFlow> sequenceFlows = getAllSequenceFlows(executionId, processDefinitionId, sequenceType);

            eMaps = Maps.newHashMap();
            eMaps.put(processDefinitionId, sequenceFlows);
            statusMap.put(executionId, eMaps);
            return sequenceFlows;
        } else {
            List<SequenceFlow> sequenceFlows = eMaps.get(processDefinitionId);
            if (CollectionUtils.isNotEmpty(sequenceFlows)) {
                return sequenceFlows;
            } else {
                sequenceFlows = getAllSequenceFlows(executionId, processDefinitionId, sequenceType);

                eMaps.put(processDefinitionId, sequenceFlows);
                statusMap.put(executionId, eMaps);
                return sequenceFlows;
            }
        }
    }

    private List<SequenceFlow> getAllSequenceFlows(String executionId, String processDefinitionId, String sequenceType) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String crruentActivityId = execution.getActivityId();
        FlowElement flowElement = bpmnModel.getFlowElement(crruentActivityId);
        FlowNode flowNode = (FlowNode) flowElement;
        List<SequenceFlow> sequenceFlows = null;
        if (FlowableConstant.SEQUENCE_FLOW_INCOMING.equals(sequenceType)) {
            sequenceFlows = flowNode.getIncomingFlows();
        } else {
            sequenceFlows = flowNode.getOutgoingFlows();
        }
        return sequenceFlows;
    }

    private List<HistoricTaskInstanceDto> listHistoryTaskInstances(String assignee) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee).list();
        List<HistoricTaskInstanceDto> results = list.stream().map(historicTaskInstance -> {
            HistoricTaskInstanceDto dto = WorkflowConverterUtil.toHistoricTaskInstanceDto(historicTaskInstance);
            return dto;
        }).collect(Collectors.toList());
        return results;
    }

    private List<HistoricActivityInstanceDto> listHistoryActivityInstances(String assignee) {
        List<HistoricActivityInstance> lists = historyService.createHistoricActivityInstanceQuery().taskAssignee(assignee).list();
        List<HistoricActivityInstanceDto> results = lists.stream().map(hai -> {
            HistoricActivityInstanceDto dto = WorkflowConverterUtil.toHistoricActivityInstanceDto(hai);
            return dto;
        }).collect(Collectors.toList());
        return results;
    }

    @Override
    public ProcessInstanceDto startProcess(String processKey, Map<String, Object> variables) {
        String userId = SysUserHandler.getCurrentUser().getId();
        Authentication.setAuthenticatedUserId(userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, variables);
        ProcessInstanceDto dto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
        this.addComment(userId, dto.getTaskId(), dto.getProcessInstanceId(), CommentTypeEnum.TJ.getDesc(), "请假提交申请");
        return dto;
    }

    @Override
    public TaskInfoDto getTaskByProcessInstanceId(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskInfoDto dto = WorkflowConverterUtil.toTaskDto(task);
        return dto;
    }

    @Override
    public TaskInfoDto getTaskByAssignee(String assigne) {
        Task task = taskService.createTaskQuery().singleResult();
        TaskInfoDto dto = WorkflowConverterUtil.toTaskDto(task);
        return dto;
    }

    @Override
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public void completeTask(String taskId, String variableKey, Object variableValue) {
        taskService.setVariable(taskId, variableKey, variableValue);
        taskService.complete(taskId);
    }

    /**
     * 获取任务节点
     *
     * @param nodeType 查询节点选择:
     *                 传入"previous"->查询上一个节点；
     *                 传入"now"->查询当前节点;
     *                 传入"next"-> 查询当前任务节点的下一个节点
     * @param taskId   任务id
     */
    public void nextFlowNode(String nodeType, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        // 当前审批节点
        String crruentActivityId = execution.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(crruentActivityId);
        // 节点的来源节点
        List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
        // 节点的目标节点
        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outFlows) {
            // 上一个审批节点
            String sourceRef = sequenceFlow.getSourceRef();
            System.out.println(sourceRef);
            //当前审批节点
            FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
            System.out.println("当前节点: id=" + sourceFlowElement.getId() + ",name=" + sourceFlowElement.getName());
            // 下一个审批节点
            FlowElement targetFlow = sequenceFlow.getTargetFlowElement();
            if (targetFlow instanceof UserTask) {
                System.out.println("下一节点: id=" + targetFlow.getId() + ",name=" + targetFlow.getName());
            }
            if (targetFlow instanceof ExclusiveGateway) {
                System.out.println("这是一个网关：id=" + targetFlow.getId() + ",name=" + targetFlow.getName());
            }
            // 如果下个审批节点为结束节点
            if (targetFlow instanceof EndEvent) {
                System.out.println("下一节点为结束节点：id=" + targetFlow.getId() + ",name=" + targetFlow.getName());
            }
            System.out.println("下一个节点类型：" + targetFlow.getClass().getName());
        }
    }

}

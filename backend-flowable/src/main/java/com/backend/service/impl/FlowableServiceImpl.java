package com.backend.service.impl;

import com.backend.common.FlowableConstant;
import com.backend.common.ResultData;
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
import com.backend.model.entity.SysUserEntity;
import com.backend.service.FlowableService;
import com.backend.util.SysUserHandler;
import com.backend.util.WorkflowConverterUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cmd.SaveCommentCmd;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.util.XmlUtil;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FlowableServiceImpl implements FlowableService {

    @Qualifier("processEngine")
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
    @Autowired
    protected ModelRepository modelRepository;
    @Autowired
    protected ModelService modelService;

    private static final BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
    private static final BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

    private String generateDefaultId() {
        String nextId = processEngine.getProcessEngineConfiguration().getIdGenerator().getNextId();
        return nextId;
    }

    @Override
    public void addComment(String userId, String taskId, String processInstanceId, String type, String msg) {
//        Command commentCmdAdd = new AddCommentCmd(taskId, processInstanceId, type, msg);
//        managementService.executeCommand(commentCmdAdd);
        taskService.addComment(taskId, processInstanceId, type, msg);
    }

    @Override
    public void addComment(String taskId, String type, String msg){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        this.addComment(null, taskId, task.getProcessInstanceId(), type, msg);
    }

    @Override
    public void updateCommentById(HiCommentDto comment) {
        Command commentUpdate = new SaveCommentCmd(comment);
        managementService.executeCommand(commentUpdate);
    }

    @Override
    public ResultData importProcessModel(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && (fileName.endsWith(".bpmn") || fileName.endsWith(".bpmn20.xml"))) {
            try {
                InputStream inputStream = file.getInputStream();
                return this.saveModel(inputStream, SysUserHandler.getCurrentUser());
            } catch (IOException e) {
                return ResultData.err("读取文件出错");
            }
        } else {
            return ResultData.err("Invalid file name, only .bpmn and .bpmn20.xml files are supported not " + fileName);
        }
    }

    @Override
    public byte[] getModelBpmnXML(String modelId) {
        Model model = modelService.getModel(modelId);
        byte[] xmlBytes = modelService.getBpmnXML(model);
        return xmlBytes;
    }

    private ResultData saveModel(InputStream inputStream, SysUserEntity currentUser) {
        try {
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            InputStreamReader xmlIn = new InputStreamReader(inputStream, "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
            BpmnModel bpmnModel = bpmnXmlConverter.convertToBpmnModel(xtr);
            //模板验证
            ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
            List<ValidationError> errors = validator.validate(bpmnModel);
            if (org.flowable.editor.language.json.converter.util.CollectionUtils.isNotEmpty(errors)) {
                StringBuffer es = new StringBuffer();
                errors.forEach(ve -> es.append(ve.toString()).append("/n"));
                return ResultData.err("模板验证失败，原因: " + es.toString());
            }
            String fileName = bpmnModel.getMainProcess().getName();
            if (org.flowable.editor.language.json.converter.util.CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                return ResultData.err("No process found in definition " + fileName);
            }
            if (bpmnModel.getLocationMap().size() == 0) {
                BpmnAutoLayout bpmnLayout = new BpmnAutoLayout(bpmnModel);
                bpmnLayout.execute();
            }
            ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);
            Process process = bpmnModel.getMainProcess();
            String name = process.getId();
            if (StringUtils.isNotEmpty(process.getName())) {
                name = process.getName();
            }
            String description = process.getDocumentation();
            User createdBy = SecurityUtils.getCurrentUserObject();
            String createByUserId = String.valueOf(currentUser.getId());
            //查询是否已经存在流程模板
            Model newModel = new Model();
            List<Model> models = modelRepository.findByKeyAndType(process.getId(), AbstractModel.MODEL_TYPE_BPMN);
            if (org.flowable.editor.language.json.converter.util.CollectionUtils.isNotEmpty(models)) {
                Model updateModel = models.get(0);
                newModel.setId(updateModel.getId());
            }
            newModel.setName(name);
            newModel.setKey(process.getId());
            newModel.setModelType(AbstractModel.MODEL_TYPE_BPMN);
            newModel.setCreated(Calendar.getInstance().getTime());
            newModel.setCreatedBy(createByUserId);
            newModel.setDescription(description);
            newModel.setModelEditorJson(modelNode.toString());
            newModel.setLastUpdated(Calendar.getInstance().getTime());
            newModel.setLastUpdatedBy(createByUserId);
            modelService.createModel(newModel, SecurityUtils.getCurrentUserObject());
            return ResultData.ok();
        } catch (Exception e) {
            log.error("Import failed for {}", e);
            return ResultData.err("Import failed for , error message : {}", e.getMessage());
        }
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
        Long userId = SysUserHandler.getCurrentUser().getId();
        Authentication.setAuthenticatedUserId(String.valueOf(userId));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, variables);
        ProcessInstanceDto dto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
        this.addComment(String.valueOf(userId), dto.getTaskId(), dto.getProcessInstanceId(), CommentTypeEnum.TJ.getDesc(), "请假提交申请");
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
        this.addComment(taskId, CommentTypeEnum.SP.getDesc(), "请假审批");
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
//        List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
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

    @Override
    public boolean existModel(String processKey) {
        return false;
    }

    private void deployment(){
        DeploymentBuilder deployment = repositoryService.createDeployment();
    }

}

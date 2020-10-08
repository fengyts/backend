package com.backend.codemodel;

import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.util.WorkflowConverterUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.MessageFlow;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;

public class HolidayRequest {

    private ProcessEngineConfiguration configuration;
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private ManagementService managementService;

    {
        init();
    }

    private void init() {
        String url = "jdbc:mysql://47.94.199.26:3306/bk_admin_flowable" +
                "?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true";
        String username = "chadmin";
        String pwd = "8-#xgC%p(t85X";
        String driverClass = "com.mysql.cj.jdbc.Driver";

        configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcUrl(url)
                .setJdbcUsername(username)
                .setJdbcPassword(pwd)
                .setJdbcDriver(driverClass)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        processEngine = configuration.buildProcessEngine();

        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
    }

    @Data
    private static class DbConfig {
        private String url;
        private String username;
        private String passwd;
        private String driverClass;
    }

    // ======================
    // 我的待办
    private void m1() {
//        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("张三").list();
        System.out.println(tasks);
    }

    // 我发起的
    public List<ActivityInstanceDto> listRuntimeTask(String assignee) {
        List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery().taskAssignee(assignee).list();
        List<ActivityInstanceDto> results = activityInstances.stream().map(ati -> {
            return WorkflowConverterUtil.toActivityInstanceDto(ati);
        }).collect(Collectors.toList());
        System.out.println(results);
        return results;
    }

    public void m2(String processInstanceId) {
        // 获取当前活动节点列表
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        List<ActivityInstance> ait = runtimeService.createActivityInstanceQuery().taskAssignee("张三").list();
        // 获取连线的列表
        String activityType = BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW;
//        String activityType = BpmnXMLConstants.ELEMENT_TASK_USER;
        List<ActivityInstance> highLightedFlowInstances = runtimeService.createActivityInstanceQuery()
                .activityType(activityType).processInstanceId(processInstanceId).list();
        System.out.println();
    }

    public void nextFlowNode(String nodeType, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        // 当前审批节点
        String crruentActivityId = execution.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(crruentActivityId);
        FlowNode flowNode = (FlowNode) flowElement;
        Map<String, MessageFlow> messageFlows = bpmnModel.getMessageFlows();
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

    public void getVariables(String executionId, String taskId) {
        Map<String, Object> variables = runtimeService.getVariables(executionId);
        Map<String, Object> variables1 = taskService.getVariables(taskId);
        System.out.println();
    }


    public static void main(String[] args) {
        HolidayRequest hr = new HolidayRequest();
        String processInstanceId = "e6b051d5-04a2-11eb-aa17-f8a2d6c46f40";
        String executionId = "e6b18a5a-04a2-11eb-aa17-f8a2d6c46f40";
        String taskId = "eadb5d0d-0596-11eb-b269-f8a2d6c46f40";
        String reqUser = "张三";
        String deptManagerUser = "部门经理";
        String companyManagerUser = "总经理";
        Integer day = 3;
//        hr.m1();
//        hr.listRuntimeTask(reqUser);
//        hr.m2(processInstanceId);
//        hr.nextFlowNode("", taskId);
        hr.getVariables(executionId, taskId);
    }

}

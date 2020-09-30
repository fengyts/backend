package com.backend.controller;

import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.component.ProcessImgGenerator;
import com.backend.entity.ProcessDefinitionDto;
import com.backend.entity.ProcessInstanceDto;
import com.backend.entity.TaskInfoDto;
import com.backend.util.WorkflowConverterUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * @author DELL
 */
@Controller
@RequestMapping("/holiday")
public class HolidayController extends BaseController {

//    @Autowired
//    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessImgGenerator generator;

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }

    @PostMapping("deployProcess")
    @ResponseBody
    public ResultData deployProcess(String processEngineName) {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/holiday-request.bpmn20.xml")
                .deploy();
        return ResultData.ok(deployment.getId());
    }

    @GetMapping("queryProcessDefinition")
    @ResponseBody
    public ResultData queryProcessDefinition(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        if (Objects.isNull(processDefinition)) {
            return ResultData.err("processDefinition 查询为空");
        }
        System.out.println("Found process definition : " + processDefinition.getName());
        ProcessDefinitionDto dto = WorkflowConverterUtil.toProcessDefinitionDto(processDefinition);
        List<ProcessDefinitionDto> results = Lists.newArrayListWithCapacity(1);
        results.add(dto);
        return ResultData.ok(results);
    }

    @GetMapping("queryProcessDefinitionList")
    @ResponseBody
    public ResultData queryProcessDefinitionList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<ProcessDefinitionDto> results = list.stream().map(p -> {
            ProcessDefinitionDto dto = WorkflowConverterUtil.toProcessDefinitionDto(p);
            return dto;
        }).collect(Collectors.toList());
        return ResultData.ok(results);
    }

    @PostMapping("startProcess")
    @ResponseBody
    public ResultData startProcess(String processEngineName, String processDefinitionKey, Map<String, Object> variables) {
        // processDefinitionKey="holidayRequest";
        variables = initVariables();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        ProcessInstanceDto dto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
        return ResultData.ok(dto);
    }

    private Map<String, Object> initVariables() {
        Map<String, Object> variables = Maps.newHashMap();
        String employee = "zhangsan";
        Integer nrOfHolidays = 7;
        String description = "famliy time";
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        return variables;
    }

    @PostMapping("approve")
    @ResponseBody
    public ResultData approve(String taskId, String approveFlag) {
        boolean approved = approveFlag.equals("y");
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("approved", approved);
        taskService.complete(taskId, variables);
        return ResultData.ok();
    }

    @GetMapping("queryTask")
    @ResponseBody
    public ResultData queryTask() {
        List<Task> tasks = taskService.createTaskQuery().list();
//        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        List<TaskInfoDto> results = tasks.stream().map(t -> {
            TaskInfoDto e = WorkflowConverterUtil.toTaskDto(t);
            return e;
        }).collect(Collectors.toList());
        return ResultData.ok(results);
    }

    @GetMapping("queryTaskByAssignee")
    @ResponseBody
    public ResultData queryTaskByAssignee(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        List<TaskInfoDto> results = tasks.stream().map(t -> {
            TaskInfoDto e = WorkflowConverterUtil.toTaskDto(t);
            return e;
        }).collect(Collectors.toList());
        return ResultData.ok(results);
    }

    public void t() {
        String processInstanceId = "60308c05-ac56-11e9-81d0-dad8d2a12195";
        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
        //获取所有节点信息
        List<Process> processes = repositoryService.getBpmnModel(definitionId).getProcesses();
        System.out.println("processes size:" + processes.size());
//        List<List<NextNode>> nextNodes = new ArrayList<>();
        for (Process process : processes) {
            Collection<FlowElement> flowElements = process.getFlowElements();
            if (CollectionUtils.isNotEmpty(flowElements)) {
                for (FlowElement flowElement : flowElements) {
                    if (flowElement instanceof UserTask) {
                        System.out.println("UserTask：" + flowElement.getName());
                        //业务操作
                    }
                    if (flowElement instanceof SubProcess) {
                        //，，，
                    }

                }
            }
        }
    }

    @GetMapping("getProcessDiagram")
    @ResponseBody
    public void getProcessDiagram(HttpServletResponse response, String processInstanceId){
        generator.genProcessDiagram(response, processInstanceId);
    }


}

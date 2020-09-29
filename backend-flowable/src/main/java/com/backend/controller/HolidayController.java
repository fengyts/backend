package com.backend.controller;

import com.backend.common.BaseController;
import com.backend.entity.ProcessDefinitionDto;
import com.backend.entity.ProcessInstanceDto;
import com.backend.entity.TaskInfoDto;
import com.backend.util.WorkflowConverterUtil;
import com.google.common.collect.Maps;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author DELL
 */
@Controller
@RequestMapping("/holiday")
public class HolidayController extends BaseController {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

//    private ProcessEngine getProcessEngineDefault() {
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        return processEngine;
//    }
//    private ProcessEngine getProcessEngine(String processEngineName) {
//        if(StringUtils.isBlank(processEngineName)){
//            return getProcessEngineDefault();
//        }
//        ProcessEngine processEngine = ProcessEngines.getProcessEngine(processEngineName);
//        return processEngine;
//    }

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }

    @PostMapping("deployProcess")
    @ResponseBody
    public String deployProcess(String processEngineName) {
//        ProcessEngine processEngine = getProcessEngineDefault();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/holiday-request.bpmn20.xml")
                .deploy();
        return deployment.getId();
    }

    @GetMapping("queryProcessDefinition")
    @ResponseBody
    public ProcessDefinitionDto queryProcessDefinition(String deploymentId){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        if (Objects.isNull(processDefinition)) {
            return new ProcessDefinitionDto();
        }
        System.out.println("Found process definition : " + processDefinition.getName());
        ProcessDefinitionDto dto = WorkflowConverterUtil.toProcessDefinitionDto(processDefinition);
        return dto;
    }

    @PostMapping("startProcess")
    @ResponseBody
    public ProcessInstanceDto startProcess(String processEngineName, String processDefinitionKey, Map<String, Object> variables) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // processDefinitionKey="holidayRequest";
        variables = initVariables();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        ProcessInstanceDto dto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
        return dto;
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
    public Boolean approve(String taskId, String approveFlag){
        boolean approved = approveFlag.equals("y");
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("approved", approved);
        taskService.complete(taskId, variables);
        return true;
    }

    @GetMapping("queryTask")
    @ResponseBody
    public List<TaskInfoDto> queryTask() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        List<TaskInfoDto> results = tasks.stream().map(t -> {
            TaskInfoDto e = WorkflowConverterUtil.toTaskDto(t);
            return e;
        }).collect(Collectors.toList());
        return results;
    }

}

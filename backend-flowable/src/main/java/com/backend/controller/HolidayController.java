package com.backend.controller;

import com.backend.common.BaseController;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
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

@Controller
@RequestMapping("/holiday")
public class HolidayController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    private ProcessEngine getProcessEngineDefault() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine;
    }

    private ProcessEngine getProcessEngine(String processEngineName) {
        if(StringUtils.isBlank(processEngineName)){
            return getProcessEngineDefault();
        }
        ProcessEngine processEngine = ProcessEngines.getProcessEngine(processEngineName);
        return processEngine;
    }

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }

    @PostMapping("deployProcess")
    @ResponseBody
    public boolean deployProcess(String processEngineName) {
//        ProcessEngine processEngine = getProcessEngine(processEngineName);
        ProcessEngine processEngine = getProcessEngineDefault();
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("process/holiday-request.bpmn20.xml")
//                .deploy();
        return true;
    }

    @PostMapping("startProcess")
    @ResponseBody
    public ProcessInstance startProcess(String processEngineName, String processDefinitionKey, Map<String, Object> variables) {
        ProcessEngine processEngine = getProcessEngine(processEngineName);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // processDefinitionKey="holidayRequest";
        variables = initVariables();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        return processInstance;
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

    @GetMapping("queryTask")
    @ResponseBody
    public List<Task> queryTask() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        return tasks;
    }

}

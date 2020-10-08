package com.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.backend.common.BaseController;
import com.backend.common.ResultData;
import com.backend.component.ProcessImgGenerator;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.util.WorkflowConverterUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;
import sun.misc.BASE64Encoder;

/**
 * @author DELL
 */
@Controller
@RequestMapping("/holidayTest")
public class HolidayTestController extends BaseController {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessImgGenerator generator;
    @Autowired
    private HistoryService historyService;

    @RequestMapping("/index")
    public String index() {
        return "/holidayTest/holidayTest";
    }

    @PostMapping("deployProcess")
    @ResponseBody
    public ResultData<DeploymentDto> deployProcess(String processEngineName) {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/HolidayProcess.bpmn20.xml")
                .deploy();
        DeploymentDto dto = WorkflowConverterUtil.toDeploymentDto(deployment);
        return ResultData.ok(dto);
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

    @GetMapping("getProcessDiagram")
    public void getProcessDiagram(HttpServletResponse response, String processInstanceId) {
        generator.processDiagram1(response, processInstanceId);

    }

    @GetMapping("getProcessDiagram1")
    public void getProcessDiagram1(HttpServletResponse response, String processInstanceId) {
        try {
//            getFloawableImgae(response, processInstanceId);
            processDiagram(response, processInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("getProcessDiagramByTaskId")
    public void getProcessDiagramByTaskId(HttpServletResponse response, String taskId) {
        genProcessDiagram(response, taskId);
    }

    public void genProcessDiagram(HttpServletResponse httpServletResponse, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processId = task.getProcessInstanceId();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();
        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        byte[] buf = new byte[1024];
        int legth = 0;
        try (
                InputStream in = diagramGenerator.generateDiagram(
                        bpmnModel,
                        "png",
                        activityIds,
                        flows,
                        1.0,
                        true);
                OutputStream out = httpServletResponse.getOutputStream();
        ) {
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("generateProcessImg")
    @ResponseBody
    public JSONObject generateProcessImg(String processInstanceId) throws IOException {

        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();

        //高亮线路id集合
//        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> highLightedFlows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        //配置字体
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, 2.0, true);
        BufferedImage bi = ImageIO.read(imageStream);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bos);
        //转换成字节
        byte[] bytes = bos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        //转换成base64串
        String png_base64 = encoder.encodeBuffer(bytes);
        //删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");

        bos.close();
        imageStream.close();
        JSONObject obj = new JSONObject();
        obj.put("img", png_base64);
        return obj;
    }


    public void getFloawableImgae(HttpServletResponse response, String processInstanceId) throws Exception {
        HistoricProcessInstance his = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(his.getProcessDefinitionId());

        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
//得到高亮的流程 processInstanceId这个应该是executionId
        List<String>
                highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
        List<String> highLightedFlows = new ArrayList<String>();
        //防止图片乱码
        InputStream in = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities,
                highLightedFlows, "宋体", "宋体", "宋体", null, 1.0, true);

        byte data[] = readInputStream(in);
        ServletOutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();

    }

    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram1(HttpServletResponse response, String processInstanceId) throws Exception {
        processDiagram(response, processInstanceId);
    }


    private void processDiagram(HttpServletResponse response, String processInstanceId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = response.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


}

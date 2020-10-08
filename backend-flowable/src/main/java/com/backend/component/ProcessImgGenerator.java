package com.backend.component;

import com.backend.diagram.CustomProcessDiagramGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessImgGenerator extends DefaultProcessDiagramGenerator {

    @Autowired
    @Qualifier("processEngine")
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
//    @Autowired
//    private ModelService modelService;

    private static final String SEQUENCE_FLOW = "sequenceFlow";
    private static final String IMAGE_TYPE_BMP = "bmp";
    private static final String IMAGE_TYPE_PNG = "png";
    private static final String IMAGE_TYPE_JPG = "jpg";
    private static final String PARAM_NULL_MESSAGE = "获取流程图异常, 参数：processInstanceId为空";

    private ProcessEngineConfiguration getProcessEngineConfig() {
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        return engconf;
    }

    private String getActivityFontName() {
        return getProcessEngineConfig().getActivityFontName();
    }

    private String getLabelFontName() {
        return getProcessEngineConfig().getLabelFontName();
    }

    private String getAnnotationFontName() {
        return getProcessEngineConfig().getAnnotationFontName();
    }

    private ClassLoader getClassLoader() {
        return getProcessEngineConfig().getClassLoader();
    }

    private InputStream generateDiagramCustomer(ProcessDiagramGenerator pdg,
                                                BpmnModel bpmnModel,
                                                String imageType,
                                                List<String> highLightedActivitis,
                                                List<String> highLightedflows) {
        if (pdg instanceof CustomProcessDiagramGenerator) {
            CustomProcessDiagramGenerator customGenerator = (CustomProcessDiagramGenerator) pdg;
            return customGenerator.generateDiagram(
                    bpmnModel,
                    IMAGE_TYPE_BMP,
                    highLightedActivitis,
                    highLightedflows,
                    getActivityFontName(),
                    getLabelFontName(),
                    getAnnotationFontName(),
                    getClassLoader(),
                    1.0,
                    true);
        }
        return pdg.generateDiagram(bpmnModel, imageType,
                getActivityFontName(), getLabelFontName(), getAnnotationFontName(),
                null, 1.0, true);
    }

    public InputStream generateProcessDiagram(BpmnModel bpmnModel) {
        return generateDiagramCustomer(this, bpmnModel, IMAGE_TYPE_PNG, null, null);
    }


    public void generateProcessDiagramByModelId(HttpServletResponse response, String modelId) {
        /*Model model = modelService.getModel(modelId);
        BpmnModel bpmnModel = modelService.getBpmnModel(model, Maps.newHashMap(), Maps.newHashMap());
        try (InputStream is = generateProcessDiagram(bpmnModel)) {
            writeResponse(response, is);
        } catch (IOException e) {
            log.info("生成流程图异常：{}", e);
        }*/
    }

    public void generateProcessDiagram(HttpServletResponse response, String processInstanceId) {
        if (StringUtils.isBlank(processInstanceId)) {
            log.info(PARAM_NULL_MESSAGE);
            return;
        }
        // 获得当前活动的节点
        String processDefinitionId = "";
        if (this.isFinished(processInstanceId)) { // 如果流程已经结束，则得到结束节点
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        } else { // 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }
        List<String> highLightedActivitis = new ArrayList<>();
        // 获得活动的节点
        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        // 高亮节点
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        List<String> highLightedflows = new ArrayList<>();
        //高亮连线
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            if (SEQUENCE_FLOW.equals(tempActivity.getActivityType())) {
                highLightedflows.add(tempActivity.getActivityId());
            }
        }
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        generateBpmnImgAndWrite(response, bpmnModel, highLightedActivitis, highLightedflows);
    }

    private boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished()
                .processInstanceId(processInstanceId).count() > 0;
    }

    @Deprecated
    public void processDiagram1(HttpServletResponse response, String processInstanceId) {
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
        generateBpmnImgAndWrite(response, bpmnModel, activityIds, flows);
    }

    /**
     * 生成流程图
     *
     * @param response
     * @param bpmnModel            bpmnModel
     * @param highLightedActivitis
     * @param highLightedflows
     */
    private void generateBpmnImgAndWrite(
            HttpServletResponse response,
            BpmnModel bpmnModel,
            List<String> highLightedActivitis,
            List<String> highLightedflows) {
//        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        byte[] buf = new byte[1024];
        int legth = 0;
        try (
                /*InputStream is = diagramGenerator.generateDiagram(
                        bpmnModel,
                        IMAGE_TYPE_BMP,
                        highLightedActivitis,
                        highLightedflows,
                        engconf.getActivityFontName(),
                        engconf.getLabelFontName(),
                        engconf.getAnnotationFontName(),
                        engconf.getClassLoader(),
                        1.0,
                        true);*/
                InputStream is = generateDiagramCustomer(diagramGenerator, bpmnModel, IMAGE_TYPE_BMP,
                        highLightedActivitis, highLightedflows);
                OutputStream out = response.getOutputStream();
        ) {
            while ((legth = is.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
            out.flush();
        } catch (IOException e) {
            log.error("生成流程图异常: {}", e);
        }
    }

    private void writeResponse(HttpServletResponse response, InputStream is) throws IOException {
        try (
                ServletOutputStream out = response.getOutputStream();
        ) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            log.error("生成流程图异常: {}", e);
            throw e;
        }

    }

}

package com.backend.service.impl;

import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.HolidayApplyForm;
import com.backend.service.FlowableService;
import com.backend.service.IHolidayService;
import com.backend.util.SysUserHandler;
import com.backend.util.WorkflowConverterUtil;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HolidayServiceImpl implements IHolidayService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowableService flowableService;

    private static final String PROCESS_KEY = "HolidayProcess";

    @Override
    public List<TaskInfoDto> listRunTimeTask(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskInfoDto> results = tasks.stream().map(t -> {
            TaskInfoDto dto = WorkflowConverterUtil.toTaskDto(t);
            return dto;
        }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<HistoricTaskInstanceDto> listHistoryTask(String assignee) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee).finished().list();
        List<HistoricTaskInstanceDto> results = list.stream()
                .map(hti -> {
                    HistoricTaskInstanceDto dto = WorkflowConverterUtil.toHistoricTaskInstanceDto(hti);
                    return dto;
                }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<HistoricProcessInstanceDto> listMyInitiateTask(SysUserEntity sysUser) {
        List<HistoricProcessInstance> myInitiates = historyService.createHistoricProcessInstanceQuery()
                .startedBy(sysUser.getId()).list();
        List<HistoricProcessInstanceDto> results = myInitiates.stream()
                .map(hi -> {
                    HistoricProcessInstanceDto dto = WorkflowConverterUtil.toHistoricProcessInstanceDto(hi);
                    return dto;
                }).collect(Collectors.toList());
        return results;
    }

    @Override
    public ProcessInstanceDto saveApply(HolidayApplyForm applyForm) {
        ProcessInstanceDto dto;
        String processInstanceId = applyForm.getProcessInstanceId();
        Map<String, Object> variables = Maps.newHashMap();
        // 设置请假提交人变量
        variables.put("reqUser", SysUserHandler.getCurrentUser().getLoginName());
        variables.put("varApply", applyForm);
        if (StringUtils.isBlank(processInstanceId)) { // 开启新流程
            // 启动流程
            dto = flowableService.startProcess(PROCESS_KEY, variables);
            return dto;
        } else { // 更新变量
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            if (null == processInstance) {
                log.info("保存出错, 流程任务不存在");
                return new ProcessInstanceDto();
            }
            dto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
            runtimeService.setVariables(processInstance.getId(), variables);
            return dto;
        }

    }

    @Override
    public void submitApply(String processInstanceId) {

    }


}

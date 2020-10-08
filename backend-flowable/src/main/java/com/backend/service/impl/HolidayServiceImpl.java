package com.backend.service.impl;

import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.service.IHolidayService;
import com.backend.util.WorkflowConverterUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayServiceImpl implements IHolidayService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

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


}

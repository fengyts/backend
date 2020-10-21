package com.backend.service.impl;

import com.backend.common.ResultData;
import com.backend.enums.StatusAuditEnum;
import com.backend.model.dto.MyInitiateTaskDto;
import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.HolidayApplyForm;
import com.backend.service.FlowableService;
import com.backend.service.IHolidayService;
import com.backend.service.IUserService;
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
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
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

    @Autowired
    private IUserService userService;

    private static final String PROCESS_KEY = "HolidayProcess";
    private static final String HOLIDAY_APPLY_VARIABLE_KEY = "varApply";

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
    public List<MyInitiateTaskDto> listMyInitiateTask(SysUserEntity sysUser) {
        List<HistoricProcessInstance> myInitiates = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(sysUser.getId())).list();
        List<MyInitiateTaskDto> results = myInitiates.stream()
                .map(hi -> {
                    MyInitiateTaskDto resultDto = new MyInitiateTaskDto();
                    HistoricProcessInstanceDto dto = WorkflowConverterUtil.toHistoricProcessInstanceDto(hi);
                    resultDto.setMyInitiate(dto);

                    setMyInitiateStatus(resultDto, sysUser.getRealName(), dto.getProcessInstanceId());
                    return resultDto;
                }).collect(Collectors.toList());
        return results;
    }

    private void setMyInitiateStatus(MyInitiateTaskDto resultDto, String currentUser, String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        StatusAuditEnum status;
        String currentNodeHandler = "";
        if (null == pi) {
            status = StatusAuditEnum.FINISHED;
            currentNodeHandler = StatusAuditEnum.FINISHED.getDesc();
        } else {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            String assignee = task.getAssignee();
            if (currentUser.equals(assignee)) {
                status = StatusAuditEnum.UNCOMMITTED;
                currentNodeHandler = currentUser;
            } else {
                status = StatusAuditEnum.UNDER_REVIEW;
                currentNodeHandler = task.getAssignee();
            }
        }
        resultDto.setStatus(status);
        resultDto.setStatusDesc(status.getDesc());
        resultDto.setCurrentNodeHandler(currentNodeHandler);
    }

    private void getCurrentNode(String executionId) {
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        // 当前审批节点
        String crruentActivityId = execution.getActivityId();
        execution.isEnded();
    }

    @Override
    public ProcessInstanceDto saveApply(HolidayApplyForm applyForm) {
        ProcessInstanceDto dto;
        String processInstanceId = applyForm.getProcessInstanceId();
        Map<String, Object> variables = Maps.newHashMap();
        // 设置请假提交人变量
        variables.put("reqUser", SysUserHandler.getCurrentUser().getRealName());
        variables.put(HOLIDAY_APPLY_VARIABLE_KEY, applyForm);

        if (StringUtils.isBlank(processInstanceId)) { // 开启新流程
            // 启动流程
            dto = flowableService.startProcess(PROCESS_KEY, variables);
            // 设置变量
            runtimeService.setVariables(dto.getExecutionId(), variables);
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
    public ResultData submitApply(HolidayApplyForm applyForm) {
        String processInstanceId = applyForm.getProcessInstanceId();
        ProcessInstanceDto processInstanceDto;
        if (StringUtils.isBlank(processInstanceId)) {
            processInstanceDto = this.saveApply(applyForm);
            processInstanceId = processInstanceDto.getProcessInstanceId();
        } else {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            processInstanceDto = WorkflowConverterUtil.toProcessInstanceDto(processInstance);
        }
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
//        TaskInfoDto taskInfoDto = WorkflowConverterUtil.toTaskDto(task);

        String executionId = task.getExecutionId();
        Map<String, Object> variables = getRuntimeVariables(executionId);
        HolidayApplyForm form = (HolidayApplyForm) variables.get(HOLIDAY_APPLY_VARIABLE_KEY);

        // 过排他网关
        TaskInfoDto taskInfoDto = flowableService.getTaskByProcessInstanceId(processInstanceId);
        Map<String, Object> variablesTask = Maps.newHashMap();
        variablesTask.put("day", form.getDay());
        SysUserEntity deptManagerUser = userService.selectById(form.getApproveUser());
        variablesTask.put("deptManagerUser", deptManagerUser.getRealName());
        flowableService.completeTask(taskInfoDto.getTaskId(), variablesTask);

        return ResultData.ok();
    }

    private void setRuntimeVariables(String executionId, Map<String, Object> vars) {
        runtimeService.setVariables(executionId, vars);
    }

    private Map<String, Object> getRuntimeVariables(String executionId) {
        Map<String, Object> variables = runtimeService.getVariables(executionId);
        return variables;
    }


}

package com.backend.service.impl;

import com.backend.common.ResultData;
import com.backend.enums.CommentTypeEnum;
import com.backend.enums.StatusAuditEnum;
import com.backend.model.dto.MyHistoryTaskDto;
import com.backend.model.dto.MyInitiateTaskDto;
import com.backend.model.dto.MyToDoTaskDto;
import com.backend.model.dto.TaskListBaseDto;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
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
    private RepositoryService repositoryService;
    @Autowired
    private FlowableService flowableService;

    @Autowired
    private IUserService userService;

    private static final String PROCESS_KEY = "HolidayProcess";
    private static final String HOLIDAY_APPLY_VARIABLE_KEY = "varApply";

    @Override
    public List<MyToDoTaskDto> listRunTimeTask(SysUserEntity currentUser) {
        String loginName = currentUser.getLoginName();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(loginName).list();
        List<MyToDoTaskDto> results = tasks.stream()
                .filter(t -> !loginName.equals(t.getAssignee())) //当前用户登陆时过滤自己提交的
                .map(t -> {
                    MyToDoTaskDto dto = new MyToDoTaskDto();
                    TaskInfoDto taskInfoDto = WorkflowConverterUtil.toTaskDto(t);
                    dto.setTaskInfo(taskInfoDto);
                    dto.setStatus(StatusAuditEnum.UNDER_REVIEW);
                    dto.setStatusDesc(StatusAuditEnum.UNDER_REVIEW.getDesc());
                    dto.setCurrentNodeHandler(userService.selectUserByLoginName(loginName).getRealName());

                    setExtraData(dto, taskInfoDto.getProcessInstanceId());
                    return dto;
                }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<MyHistoryTaskDto> listHistoryTask(SysUserEntity currentUser) {
        String loginName = currentUser.getLoginName();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(loginName).finished().list();
        List<MyHistoryTaskDto> results = list.stream()
                .filter(t -> !loginName.equals(t.getAssignee())) //当前用户登陆时过滤自己提交的
                .map(hti -> {
                    MyHistoryTaskDto dto = new MyHistoryTaskDto();
                    HistoricTaskInstanceDto htDto = WorkflowConverterUtil.toHistoricTaskInstanceDto(hti);
                    dto.setHistoricTask(htDto);

                    String processInstanceId = htDto.getProcessInstanceId();
                    setTaskStatus(dto, SysUserHandler.getCurrentUser().getRealName(), processInstanceId);
                    setExtraData(dto, processInstanceId);
                    return dto;
                }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<MyInitiateTaskDto> listMyInitiateTask(SysUserEntity sysUser) {
        List<HistoricProcessInstance> myInitiates = getHistoricProcessIns(sysUser.getId());
        List<MyInitiateTaskDto> results = myInitiates.stream()
                .map(hi -> {
                    MyInitiateTaskDto resultDto = new MyInitiateTaskDto();
                    HistoricProcessInstanceDto dto = WorkflowConverterUtil.toHistoricProcessInstanceDto(hi);
                    resultDto.setMyInitiate(dto);

                    setTaskStatus(resultDto, sysUser.getRealName(), dto.getProcessInstanceId());
                    setExtraData(resultDto, dto.getProcessInstanceId());
                    return resultDto;
                }).collect(Collectors.toList());
        return results;
    }

    private List<HistoricProcessInstance> getHistoricProcessIns(Long userId) {
        List<HistoricProcessInstance> hps = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(userId)).list();
        return hps;
    }

    private void setExtraData(TaskListBaseDto dto, String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String startUserId = historicProcessInstance.getStartUserId();
        Long userId = Long.valueOf(startUserId);
        SysUserEntity sysUserEntity = userService.selectById(userId);
        dto.setApplyUser(sysUserEntity.getRealName());

        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        dto.setTaskType(processDefinition.getName());
    }

    private void setTaskStatus(TaskListBaseDto resultDto, String currentUserRealName, String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        StatusAuditEnum status;
        String currentNodeHandler = "";
        if (null == currentUserRealName) {
            currentUserRealName = "";
        }
        if (null == pi) {
            status = StatusAuditEnum.FINISHED;
        } else {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            String assignee = task.getAssignee();
            String assigneeName = userService.selectUserByLoginName(assignee).getRealName();
            if (currentUserRealName.equals(assigneeName)) {
                status = StatusAuditEnum.UNCOMMITTED;
                currentNodeHandler = currentUserRealName;
            } else {
                status = StatusAuditEnum.UNDER_REVIEW;
                currentNodeHandler = assigneeName;
            }
            if (resultDto instanceof MyInitiateTaskDto) {
                MyInitiateTaskDto myInitiateTaskDto = (MyInitiateTaskDto) resultDto;
                myInitiateTaskDto.getMyInitiate().setName(task.getName());
            }
        }
        resultDto.setStatus(status);
        resultDto.setStatusDesc(status.getDesc());
        resultDto.setCurrentNodeHandler(currentNodeHandler);
    }

    @Override
    public ProcessInstanceDto saveApply(HolidayApplyForm applyForm) {
        ProcessInstanceDto dto;
        String processInstanceId = applyForm.getProcessInstanceId();
        Map<String, Object> variables = Maps.newHashMap();
        // 设置请假提交人变量
        variables.put("reqUser", SysUserHandler.getCurrentUser().getLoginName());
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
        }
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        String executionId = task.getExecutionId();
        Map<String, Object> variables = getRuntimeVariables(executionId);
        HolidayApplyForm form = (HolidayApplyForm) variables.get(HOLIDAY_APPLY_VARIABLE_KEY);

        // 过排他网关
        TaskInfoDto taskInfoDto = flowableService.getTaskByProcessInstanceId(processInstanceId);
        Map<String, Object> variablesTask = Maps.newHashMap();
        variablesTask.put("day", form.getDay());
        SysUserEntity deptManagerUser = userService.selectById(form.getApproveUser());
        variablesTask.put("deptManagerUser", deptManagerUser.getLoginName());

        flowableService.completeTask(taskInfoDto, variablesTask, CommentTypeEnum.TJ, "提交请假申请");

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

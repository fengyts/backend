package com.backend.controller;

import com.backend.common.FlowableConstant;
import com.backend.common.ResultData;
import com.backend.enums.AuditTypeEnum;
import com.backend.model.dto.flowable.ActivityInstanceDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.HolidayApplyForm;
import com.backend.service.FlowableService;
import com.backend.service.IHolidayService;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/holiday")
@Deprecated
public class HolidayController extends FlowableBaseController {

    @Autowired
    private FlowableService flowableService;
    @Autowired
    private IHolidayService holidayService;

    // 流程的key：即流程定义文件中的 process的id
    private static final String PROCESS_KEY = "HolidayProcess";

    @RequestMapping("/list")
    public String list() {
        return "/holiday/list";
    }

    @RequestMapping("/applyList")
    public String applyList() {
        return "/holiday/applyList";
    }

    /**
     * 获取我的申请列表数据
     *
     * @return
     */
    @GetMapping("getApplyListData")
    @ResponseBody
    public ResultData<List<ActivityInstanceDto>> getRuntimeApplyListData(String assigne) {
        if (StringUtils.isBlank(assigne)) {
            return ResultData.errParam();
        }
        List<ActivityInstanceDto> atiList = flowableService.listRuntimeTask(assigne);
        return ResultData.ok(atiList);
    }

    /**
     * 请假申请 保存
     *
     * @param applyForm 用户保存请假申请表单对象
     * @return
     */
    @PostMapping("/saveApply")
    @ResponseBody
    public ResultData saveApply(HolidayApplyForm applyForm) {
        // 参数校验
        boolean validate = validateParam(applyForm);
        if (!validate) {
            return ResultData.errParam();
        }
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("reqUser", getCurrentLoginUser().getLoginName());

        // 启动流程
        ProcessInstanceDto dto = flowableService.startProcess(PROCESS_KEY, variables);
        return ResultData.ok(dto);
    }

    /**
     * 手动提交(保存后提交 | 驳回后再次提交)
     *
     * @param applyForm applyForm
     * @return
     */
    @GetMapping("manualSubmit")
    @ResponseBody
    public ResultData manualComplete(HolidayApplyForm applyForm) {
        // 参数校验
        boolean validate = validateParam(applyForm);
        if (!validate) {
            return ResultData.errParam();
        }
        String assignee = getCurrentLoginUser().getLoginName();
        String taskId;
        TaskInfoDto taskInfoDto = flowableService.getTaskByAssignee(assignee);
        if (null == taskInfoDto) { // 任务不存在，开启任务
            // 启动流程
            Map<String, Object> variables = Maps.newHashMap();
            variables.put("reqUser", getCurrentLoginUser().getLoginName());
            ProcessInstanceDto dto = flowableService.startProcess(PROCESS_KEY, variables);
            // 自动过排他网关
            taskInfoDto = flowableService.getTaskByProcessInstanceId(dto.getProcessInstanceId());
            taskId = taskInfoDto.getTaskId();
        } else {
            taskId = taskInfoDto.getId();
        }
        Map<String, Object> variablesTask = Maps.newHashMap();
        variablesTask.put("day", applyForm.getDay());
        flowableService.completeTask(taskId, variablesTask);
        return ResultData.ok(taskInfoDto);
    }

    /**
     * 请假申请
     *
     * @param applyForm   用户id
     * @return
     */
    @PostMapping("apply")
    @ResponseBody
    public ResultData apply(HolidayApplyForm applyForm) {
//        Long userId = applyForm.getUserId();
//        String userName = applyForm.getUserName();
        SysUserEntity sysUser = getCurrentLoginUser();
        String userId = sysUser.getId();
        String userName = sysUser.getLoginName();
        Integer day = applyForm.getDay();
        // 参数校验
        boolean validate = validateParam(applyForm);
        if (!validate) {
            return ResultData.errParam();
        }
//        if(tasks!=null&&tasks.size()!=0){
//            return "任务已经提交,正在处理中.........";
//        }

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("reqUser", userName);
        variables.put("day", day);
//        variables.put("deptManagerUser", "部门经理");
//        variables.put("componyManagerUser", "总经理");
        variables.put("deptManagerUser", "wangwu"); // 组长审批
        variables.put("componyManagerUser", "boss"); // 老板审批

        // 启动流程
        ProcessInstanceDto dto = flowableService.startProcess(PROCESS_KEY, variables);
        // 自动过排他网关
        TaskInfoDto taskInfoDto = flowableService.getTaskByProcessInstanceId(dto.getProcessInstanceId());
        Map<String, Object> variablesTask = Maps.newHashMap();
        variablesTask.put("day", day);
        flowableService.completeTask(taskInfoDto.getId(), variablesTask);
        return ResultData.ok(dto);
    }

    private static boolean validateParam(HolidayApplyForm applyForm) {
        if (null == applyForm.getDay() || applyForm.getDay() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取我的待审核列表
     *
     * @return
     */
    @RequestMapping("/auditList")
    public String auditList() {
        return "/holiday/auditList";
    }

    /**
     * 审核：通过/驳回
     *
     * @param taskId       任务id
     * @param approveFlag  审核：通过/驳回 标识
     * @param rejectReason 驳回时填写驳回原因
     * @return
     */
    @PostMapping("/approve")
    @ResponseBody
    public ResultData approve(String taskId, Boolean approveFlag, String rejectReason) {
        if (StringUtils.isBlank(taskId)) {
            return ResultData.errParam();
        }
        Map<String, Object> variables = Maps.newHashMap();
        if(!approveFlag){
            if(StringUtils.isBlank(rejectReason)){
                return ResultData.err("驳回时驳回原因必填!");
            }
            variables.put("rejectReason", rejectReason);
        }
        // 通过或者驳回: 网关根据变量-outcom的值来判断通过/驳回
        AuditTypeEnum auditType = approveFlag ? AuditTypeEnum.PASS : AuditTypeEnum.REJECT;
        variables.put(FlowableConstant.APPROVE, auditType.getCode());
        flowableService.completeTask(taskId, variables);
        return ResultData.ok();
    }

    /**
     * 查看流程图
     *
     * @param response
     * @param processInstanceId
     * @throws Exception
     */
    @RequestMapping("/processDiagram")
    public void generateProcessDiagram(HttpServletResponse response, String processInstanceId) throws Exception {
        generator.generateProcessDiagram(response, processInstanceId);
    }

}

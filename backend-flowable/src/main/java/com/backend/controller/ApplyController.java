package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.enums.HolidayTypeEnum;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.HolidayApplyForm;
import com.backend.service.IHolidayService;
import com.backend.service.IUserService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apply")
public class ApplyController extends FlowableBaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IHolidayService holidayService;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("holidayTypes", HolidayTypeEnum.values());
        List<SysUserEntity> allUsers = userService.listAllUsers();
        model.addAttribute("approveUsers", allUsers);
        return "apply/applyList";
    }

    /**
     * 请假申请-保存
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
        ProcessInstanceDto dto = holidayService.saveApply(applyForm);
        return ResultData.ok(dto);
    }

    /**
     * 请假申请-提交
     *
     * @param
     * @return
     */
    @PostMapping("/submitApply")
    @ResponseBody
    public ResultData submitApply(HolidayApplyForm applyForm) {
        // 参数校验
        String processInstanceId = applyForm.getProcessInstanceId();
        if (StringUtils.isBlank(processInstanceId)) {
            boolean validate = validateParam(applyForm);
            if (!validate) {
                return ResultData.errParam();
            }
        }
        ResultData resultData = holidayService.submitApply(applyForm);
        return resultData;
    }

    private boolean validateParam(HolidayApplyForm applyForm) {
        if (null == applyForm.getDay() || applyForm.getDay() <= 0) {
            return false;
        }
        if (StringUtils.isBlank(applyForm.getReason())) {
            return false;
        }
        if (null == applyForm.getApproveUser()) {
            return false;
        }
        return true;
    }


}

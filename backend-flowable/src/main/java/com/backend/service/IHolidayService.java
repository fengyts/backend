package com.backend.service;

import com.backend.common.ResultData;
import com.backend.model.dto.MyHistoryTaskDto;
import com.backend.model.dto.MyInitiateTaskDto;
import com.backend.model.dto.MyToDoTaskDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.ProcessInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import com.backend.model.form.HolidayApplyForm;
import java.util.List;

public interface IHolidayService {

    /**
     * 获取待办
     *
     * @param currentUser 当前登陆用户
     * @return
     */
    List<MyToDoTaskDto> listRunTimeTask(SysUserEntity currentUser);

    /**
     * 获取我已办理的任务
     *
     * @param currentUser
     * @return
     */
    List<MyHistoryTaskDto> listHistoryTask(SysUserEntity currentUser);

    /**
     * 获取我发起的任务
     *
     * @param sysUser
     * @return
     */
    List<MyInitiateTaskDto> listMyInitiateTask(SysUserEntity sysUser);

    ProcessInstanceDto saveApply(HolidayApplyForm applyForm);

    ResultData submitApply(HolidayApplyForm applyForm);

}

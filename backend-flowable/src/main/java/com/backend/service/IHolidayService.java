package com.backend.service;

import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.model.entity.SysUserEntity;
import java.util.List;

public interface IHolidayService {

    /**
     * 获取待办
     *
     * @param assignee 当前登陆用户名
     * @return
     */
    List<TaskInfoDto> listRunTimeTask(String assignee);

    /**
     * 获取我已办理的任务
     *
     * @param assignee
     * @return
     */
    List<HistoricTaskInstanceDto> listHistoryTask(String assignee);

    /**
     * 获取我发起的任务
     *
     * @param sysUser
     * @return
     */
    List<HistoricProcessInstanceDto> listMyInitiateTask(SysUserEntity sysUser);


}
package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.model.dto.flowable.HistoricProcessInstanceDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.service.IHolidayService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 任务列表controller
 */
@Controller
@RequestMapping("/task")
public class TaskController extends FlowableBaseController {

    @Autowired
    private IHolidayService holidayService;

    @GetMapping("/list")
    public String taskList(Model model){
        setCurrentUser(model);
        return "/task/taskList";
    }

    /**
     * 获取待办任务
     * @return
     */
    @GetMapping("getToDoTasks")
    @ResponseBody
    public ResultData listToDoTasks(){
        String assignee = getCurrentLoginUser().getLoginName();
        List<TaskInfoDto> taskList = holidayService.listRunTimeTask(assignee);
        return ResultData.ok(taskList);
    }

    /**
     * 获取已办任务
     * @return
     */
    @GetMapping("getHistoryTask")
    @ResponseBody
    public ResultData listHistoryTask(){
        String assignee = getCurrentLoginUser().getLoginName();
        List<HistoricTaskInstanceDto> historyTaskList = holidayService.listHistoryTask(assignee);
        return ResultData.ok(historyTaskList);
    }

    /**
     * 获取我发起的任务
     * @return
     */
    @GetMapping("getMyInitiateTask")
    @ResponseBody
    public ResultData listMyInitiateTask(){
        List<HistoricProcessInstanceDto> myInitiateTask = holidayService.listMyInitiateTask(getCurrentLoginUser());
        return ResultData.ok(myInitiateTask);
    }

}

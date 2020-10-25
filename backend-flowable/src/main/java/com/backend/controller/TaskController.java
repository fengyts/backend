package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.model.dto.MyHistoryTaskDto;
import com.backend.model.dto.MyInitiateTaskDto;
import com.backend.model.dto.MyToDoTaskDto;
import com.backend.model.dto.flowable.HistoricTaskInstanceDto;
import com.backend.model.dto.flowable.TaskInfoDto;
import com.backend.service.IHolidayService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String taskList(){
        return "/task/taskList";
    }

    /**
     * 获取待办任务
     * @return
     */
    @GetMapping("getToDoTasks")
    @ResponseBody
    public ResultData listToDoTasks(){
        List<MyToDoTaskDto> taskList = holidayService.listRunTimeTask(getCurrentLoginUser());
        return ResultData.ok(taskList);
    }

    /**
     * 获取已办任务
     * @return
     */
    @GetMapping("getHistoryTask")
    @ResponseBody
    public ResultData listHistoryTask(){
        List<MyHistoryTaskDto> historyTaskList = holidayService.listHistoryTask(getCurrentLoginUser());
        return ResultData.ok(historyTaskList);
    }

    /**
     * 获取我发起的任务
     * @return
     */
    @GetMapping("getMyInitiateTask")
    @ResponseBody
    public ResultData listMyInitiateTask(){
        List<MyInitiateTaskDto> myInitiateTask = holidayService.listMyInitiateTask(getCurrentLoginUser());
        return ResultData.ok(myInitiateTask);
    }

}

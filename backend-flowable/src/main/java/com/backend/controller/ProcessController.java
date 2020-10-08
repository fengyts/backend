package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.service.FlowableService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/process")
public class ProcessController extends FlowableBaseController{

//    @Autowired
//    private ModelRepository modelRepository;

    @Autowired
    private FlowableService flowableService;

    @RequestMapping("/processList")
    public String processList() {
        return "/holiday/processList";
    }

    /**
     * 获取部署流程列表数据
     *
     * @return
     */
    @GetMapping("getDeploymentListData")
    @ResponseBody
    public ResultData<List<DeploymentDto>> getProcessListData() {
        List<DeploymentDto> deploymentList = flowableService.listDeployment();
        return ResultData.ok(deploymentList);
    }

    /**
     * 获取流程定义列表数据
     *
     * @return
     */
    @GetMapping("getProcessDefinitionListData")
    @ResponseBody
    public ResultData<List<ProcessDefinitionDto>> getProcessDefinitionListData() {
        List<ProcessDefinitionDto> definitionListList = flowableService.listProcessDefinition();
        return ResultData.ok(definitionListList);
    }

    @GetMapping(value = "/loadPngByModelId/{modelId}")
    public void loadPngByModelId(@PathVariable String modelId, HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png");
        generator.generateProcessDiagramByModelId(response, modelId);
    }

}

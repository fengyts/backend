package com.backend.controller;

import com.backend.common.ResultData;
import com.backend.model.dto.flowable.DeploymentDto;
import com.backend.model.dto.flowable.ProcessDefinitionDto;
import com.backend.service.FlowableService;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/process")
@Slf4j
public class ProcessController extends FlowableBaseController {

    @Autowired
    private FlowableService flowableService;

    @RequestMapping("/processList")
    public String processList() {
        return "/process/processList";
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

    @GetMapping(value = "/loadImgByModelId/{modelId}")
    public void loadImgByModelId(@PathVariable String modelId, HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png");
        generator.generateProcessDiagramByModelId(response, modelId);
    }

    @PostMapping("importProcessModel")
    public ResultData importProcessModel(@RequestParam("file") MultipartFile file){
        ResultData resultData = flowableService.importProcessModel(file);
        return resultData;
    }

    @GetMapping("/loadXmlByModelId/{modelId}")
    public void loadXmlByModelId(@PathVariable String modelId, HttpServletResponse response) {
        try (
                ServletOutputStream outputStream = response.getOutputStream();
        ) {
            byte[] xmlBytes = flowableService.getModelBpmnXML(modelId);
            response.setHeader("Content-type", "text/xml;charset=UTF-8");
            outputStream.write(xmlBytes);
            outputStream.flush();
        } catch (Exception e) {
            log.error("ApiFlowableModelResource-loadXmlByModelId:" + e);
        }
    }

}

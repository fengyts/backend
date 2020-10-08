//package com.backend.component;
//
//import java.io.InputStream;
//import org.flowable.bpmn.model.BpmnModel;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.ProcessEngine;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.engine.test.Deployment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BpmnConverterUtil {
//
//    @Autowired
//    private ProcessEngine processEngine;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private HistoryService historyService;
//    @Autowired
//    private RepositoryService repositoryService;
//    @Autowired
//    private TaskService taskService;
//
//    @Deployment(resource="resource/leave.bpmn")
//    public void testXmltoBpmn(){
//        ProcessDefinition processDefinition = repositoryService
//                .createProcessDefinitionQuery()
//                .processDefinitionKey("leave");
//        //获取流程资源的名称
//        String sourceName = processDefinition.getResourceName();
//        //获取流程资源
//        InputStream inputStream = repositoryService.getResourceAsStream(
//                processitionDefinition.getId(),sourceName);
//        //创建转换对象
//        BpmnXmlConverter comverter = new BpmnXmlConverter();
//        //读取xml文件
//        XmlInputFactory factory = XmlInputFactory.newInstance();
//        XmlStreamReader reader = factory.createXmlStreamReader(inputStream);
//        //将xml文件转换成BpmnModel
//        BpmnModel bpmnModel = converter.converterToBpmnModel(reader);
//        //验证bpmnModel是否为空
//        assertNotNull(bpmnModel);
//        Process process = bpmnModel.getMainProcess();
//        //验证转换的流程id
//        assertEquals("leave",process.getId());
//    }
//
//    @Deploy(resource="resource/leave.bmpn")
//    public void BpmnModeltoXml(){
//        ProcessDefinition processDefinition =    repositoryService
//                .createProcessDefinitionQuery()
//                .processDefinitionKey("leave");
//        //获取BpmnModel对象
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
//        //创建转换对象
//        BpmnXmlConverter converter = new BpmnXmlConverter();
//        //把bpmnModel对象转换成字符
//        byte[] bytes = converter.converterToXML(bpmnModle);
//        String xmlContenxt = bytes.toString();
//        System.out.println(xmlContenxt);
//    }
//
//}

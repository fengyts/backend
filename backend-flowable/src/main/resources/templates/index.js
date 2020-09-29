layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer;

    $("#deployProcess").click(function(){
        $.post("/holiday/deployProcess", function (res){
            console.log(res);
        }, 'text')
    });

    $("#queryProcessDefinition").click(function () {
        // let deploymentId = "holidayRequest:1:adb8e163-01f2-11eb-b48a-52234379a457";
        let deploymentId = "ad988821-01f2-11eb-b48a-52234379a457";
        $.get("/holiday/queryProcessDefinition?deploymentId=" + deploymentId, function (res) {
            console.log(res);
        },'json');
    });

    $("#startProcess").click(function (){
        $.post("/holiday/startProcess", {"processDefinitionKey":"holidayRequest"}, function (res) {

        }, 'json');
    });

    $("#approve").click(function () {
        let taskId = "e212b21b-022a-11eb-9874-52234379a457";
        let approveFlag = "y";
        $.post("/holiday/approve", {"taskId":taskId,"approveFlag":approveFlag}, function (res) {
            console.log(res);
        }, 'text');
    });
    
    $("#viewTask").click(function () {
        $.get("/holiday/queryTask",function (res){
            console.log(res);
        }, 'json');
    });

});
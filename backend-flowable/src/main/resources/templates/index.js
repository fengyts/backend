layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form,
        table = layui.table,
        util = layui.util;

    var deploymentId = "ad988821-01f2-11eb-b48a-52234379a457";
    var queryProcessList = table.render({
        elem: '#queryProcessList',
        url: "/holiday/queryProcessDefinition?deploymentId=" + deploymentId,
        method: 'get',
        // cellMinWidth: 80,
        // height: 'full-130',
        cols: [
            [
                // {type: 'radio', width: 30, fixed: 'left'},
                {field: 'id', title: 'id', minWidth: 410, align: 'center'},
                {field: 'deploymentId', title: 'deploymentId', minWidth: 300, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {field: 'key', title: 'key',minWidth: 150, align: 'center'},
                {field: 'resourceName', title: '流程图', minWidth: 300, align: 'center'},
            ]
        ],
        page: false,
        parseData: function (res) {
            // console.log(res);
            return {
                "code": '',
                // "msg": res.msg,
                // "count": res.total,
                "data": res.data
            };
        }
    });
    var viewTaskList = table.render({
        elem: '#viewTaskList',
        url: "/holiday/queryTask",
        method: 'get',
        // cellMinWidth: 80,
        // height: 'full-130',
        cols: [
            [
                // {type: 'radio', width: 30, fixed: 'left'},
                {field: 'id', title: 'id',minWidth: 290, align: 'center'},
                {field: 'executionId', title: 'executionId', minWidth: 300, align: 'center'},
                {field: 'deploymentId', title: 'deploymentId', minWidth: 100, align: 'center'},
                {field: 'name', title: '名称', minWidth: 200, align: 'center'},
                {field: 'processDefinitionId', title: 'processDefinitionId', width: 415, align: 'center'},
                {field: 'processInstanceId', title: 'processInstanceId', minWidth: 290, align: 'center'},
                {field: 'taskDefinitionKey', title: 'taskDefinitionKey', minWidth: 150, align: 'center'},
                {field: 'createTime', title: '创建时间', minWidth: 200, align: 'center',templet:(rowData)=>{
                       return util.toDateString(rowData.createTime,'yyyy-MM-dd HH:mm:ss');
                    }},
            ]
        ],
        page: false,
        parseData: function (res) {
            // console.log(res);
            return {
                "code": '',
                // "msg": res.msg,
                // "count": res.total,
                "data": res.data
            };
        }
    });

    $("#deployProcess").click(function () {
        $.post("/holiday/deployProcess", function (res) {
            console.log(res);
        }, 'text')
    });

    $("#queryProcessDefinition").click(function () {
        queryProcessList.reload();
        // let deploymentId = "holidayRequest:1:adb8e163-01f2-11eb-b48a-52234379a457";
        // let deploymentId = "ad988821-01f2-11eb-b48a-52234379a457";
        // $.get("/holiday/queryProcessDefinition?deploymentId=" + deploymentId, function (res) {
        //     console.log(res);
        // }, 'json');
    });

    $("#startProcess").click(function () {
        $.post("/holiday/startProcess", {"processDefinitionKey": "holidayRequest"}, function (res) {

        }, 'json');
    });

    $("#approve").click(function () {
        let taskId = "e212b21b-022a-11eb-9874-52234379a457";
        let approveFlag = "y";
        $.post("/holiday/approve", {"taskId": taskId, "approveFlag": approveFlag}, function (res) {
            console.log(res);
        }, 'text');
    });

    $("#viewTask").click(function () {
        viewTaskList.reload();
        // $.get("/holiday/queryTask", function (res) {
        //     console.log(res);
        // }, 'json');
    });

});
layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form,
        table = layui.table,
        util = layui.util;

    var deploymentId = "ad988821-01f2-11eb-b48a-52234379a457";
    var queryProcessList = table.render({
        elem: '#queryProcessList',
        // url: "/holidayTest/queryProcessDefinition?deploymentId=" + deploymentId,
        url: "/holidayTest/queryProcessDefinitionList",
        method: 'get',
        // cellMinWidth: 80,
        // height: 'full-130',
        // toolbar: '<div><span style="height:10px">流程列表</span></div>',
        toolbar: '<div><span style="height: 10px; font-size: 12px;">流程列表</span></div>',
        defaultToolbar: [],
        cols: [
            [
                // {type: 'radio', width: 30, fixed: 'left'},
                {field: 'id', title: 'id', minWidth: 410, align: 'center'},
                {field: 'deploymentId', title: 'deploymentId', minWidth: 300, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {field: 'key', title: 'key', minWidth: 150, align: 'center'},
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
        url: "/holidayTest/queryTask",
        method: 'get',
        // cellMinWidth: 80,
        // height: 'full-130',
        toolbar: '<div><span style="height: 10px;font-size: 12px;">待处理列表</span></div>',
        defaultToolbar: [],
        cols: [
            [
                {
                    title: '操作', width: 110, fixed: 'left', templet: rowData => {
                        return '<span class="layui-btn layui-btn-xs" lay-event="pass">通过</span>' +
                            '<span class="layui-btn layui-btn-xs" lay-event="reject">驳回</span>';
                    }
                },
                {field: 'id', title: 'taskId', minWidth: 300, align: 'center'},
                {field: 'executionId', title: 'executionId', minWidth: 300, align: 'center'},
                // {field: 'deploymentId', title: 'deploymentId', minWidth: 100, align: 'center'},
                {field: 'name', title: '名称', minWidth: 200, align: 'center'},
                {field: 'processDefinitionId', title: 'processDefinitionId', width: 415, align: 'center'},
                {field: 'processInstanceId', title: 'processInstanceId', minWidth: 300, align: 'center'},
                {field: 'taskDefinitionKey', title: 'taskDefinitionKey', minWidth: 200, align: 'center'},
                {
                    field: 'createTime', title: '创建时间', minWidth: 200, align: 'center', templet: (rowData) => {
                        return util.toDateString(rowData.createTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
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

    // 监听事件
    table.on('tool(viewTaskList)', function (obj) {
        let rowData = obj.data;
        let taskId = rowData.id;
        switch (obj.event) {
            case 'pass':
                approved(taskId, "y");
                return;
            case 'reject':
                approved(taskId, "n");
                return;
            default:
                return;
        }
    });

    $("#deployProcess").click(function () {
        $.post("/holidayTest/deployProcess", function (res) {
            console.log(res);
        }, 'text')
    });

    $("#queryProcessDefinition").click(function () {
        queryProcessList.reload();
        // let deploymentId = "holidayRequest:1:adb8e163-01f2-11eb-b48a-52234379a457";
        // let deploymentId = "ad988821-01f2-11eb-b48a-52234379a457";
        // $.get("/holidayTest/queryProcessDefinition?deploymentId=" + deploymentId, function (res) {
        //     console.log(res);
        // }, 'json');
    });

    $("#startProcess").click(function () {
        $.post("/holidayTest/startProcess", {"processDefinitionKey": "holidayRequest"}, function (res) {

        }, 'json');
    });

    $("#approve").click(function () {
        let taskId = "e212b21b-022a-11eb-9874-52234379a457";
        let approveFlag = "y";
        $.post("/holidayTest/approve", {"taskId": taskId, "approveFlag": approveFlag}, function (res) {
            console.log(res);
        }, 'json');
    });

    function approved(taskId, approveFlag) {
        $.post("/holidayTest/approve", {"taskId": taskId, "approveFlag": approveFlag}, function (res) {
            console.log(res);
            if ("0" === res.code) {
                layer.msg("操作成功", {time: 1500});
            } else {
                layer.msg(res.msg, {icon: 1, time: 1500});
            }
        }, 'json');
    }

    $("#viewMyReqTask").click(function () {
        // let assignee = "zhangsan";
        let assignee = $("input[name='assignee']").val();
        $.get("/holidayTest/queryTaskByAssignee?assignee=" + assignee, function (res) {
            console.log(res);
        }, 'json');
    });

    $("#viewTask").click(function () {
        viewTaskList.reload();
        // $.get("/holidayTest/queryTask", function (res) {
        //     console.log(res);
        // }, 'json');
    });

    $("#viewProcess").click(function () {
        let processInstanceId = $("input[name='processInstanceId']").val();
        layer.open({
            title: '流程图',
            type: 2,
            area: ['800px','450px'],
            // content: '/holidayTest/getProcessDiagram?processInstanceId=' + processInstanceId
            content: '/holidayTest/generateProcessImg?processInstanceId=' + processInstanceId
        });
    });

    $("#viewProcessByTaskId").click(function () {
        let taskId = $("input[name='taskId']").val();
        console.log(taskId);
        layer.open({
            title: '流程图',
            type: 2,
            area: ['800px','450px'],
            content: '/holidayTest/getProcessDiagramByTaskId?taskId=' + taskId
        });
    });

});
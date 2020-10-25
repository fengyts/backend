layui.use(['element', 'form', 'table', 'util'], function () {
    var element = layui.element,
        form = layui.form,
        table = layui.table,
        util = layui.util;


    // 初始化表格 - 待办列表
    var todoTask = table.render({
        elem: '#todoTask',
        url: "/task/getToDoTasks",
        method: 'get',
        height: 'full-150',
        cols: [
            [
                {
                    title: '操作', minWidth: 150, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">流程图</span>',
                            _audit = '<span class="layui-btn layui-btn-xs" lay-event="approve">审核</span>';
                        return _html + _audit;
                    }
                },
                {
                    field: 'id', hide: 'true', title: '任务ID', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData['taskInfo'].id;
                    }
                },
                {
                    field: 'processInstanceId',
                    hide: 'true',
                    title: '流程实例ID',
                    minWidth: 290,
                    align: 'center',
                    templet: rowData => {
                        return rowData['taskInfo'].processInstanceId;
                    }
                },
                {
                    field: 'type', title: '类型', minWidth: 120, align: 'center', templet: rowData => {
                        return rowData.taskType;
                    }
                },
                {
                    field: 'applyUser', title: '申请人', minWidth: 80, align: 'center', templet: rowData => {
                        return rowData.applyUser;
                    }
                },
                {
                    field: 'activityName', title: '当前节点', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData['taskInfo'].name
                    }
                },
                {
                    field: 'assignee', title: '审批人', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.currentNodeHandler;
                    }
                },
                {
                    field: 'statusAudit', title: '状态', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.statusDesc;
                    }
                },
                {
                    field: 'deploymentTime',
                    title: '申请时间',
                    minWidth: 160,
                    align: 'center',
                    templet: rowData => {
                        return util.toDateString(rowData['taskInfo'].createTime, 'yyyy-MM-dd HH:mm:ss');
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
        },
        // 解决页面无数据时表头显示不全, 希望出现水平滚动条
        done: function (res, curr, count) {
            if (res.data && res.data.length == 0) {
                count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
                count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
            }
        }
    });

    // 初始化表格 - 已办列表
    var historyTaskList = table.render({
        elem: '#historyTaskList',
        url: "/task/getHistoryTask",
        method: 'get',
        height: 'full-150',
        cols: [
            [
                {
                    title: '操作', minWidth: 150, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">流程图</span>';
                        return _html;
                    }
                },
                {
                    title: '任务ID', hide: 'true', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData['historicTask'].id;
                    }
                },
                {
                    title: '流程实例ID', hide: 'true', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData['historicTask'].processInstanceId;
                    }
                },
                {
                    field: 'type', title: '类型', minWidth: 120, align: 'center', templet: rowData => {
                        return rowData.taskType;
                    }
                },
                {
                    field: 'applyUser', title: '申请人', minWidth: 80, align: 'center', templet: rowData => {
                        return rowData.applyUser;
                    }
                },
                {
                    field: 'activityName', title: '当前节点', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.currentNodeHandler;
                    }
                },
                {
                    field: 'assignee', title: '审批人', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData['historicTask'].name;
                    }
                },
                {
                    field: 'statusAudit', title: '状态', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.statusDesc;
                    }
                },
                {
                    field: 'deploymentTime', title: '创建时间', minWidth: 160, align: 'center',
                    templet: rowData => {
                        return util.toDateString(rowData.createTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    field: 'deploymentTime', title: '处理时间', minWidth: 160, align: 'center',
                    templet: rowData => {
                        return util.toDateString(rowData.createTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                }
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
        },
        // 解决页面无数据时表头显示不全, 希望出现水平滚动条
        done: function (res, curr, count) {
            if (res.data && res.data.length == 0) {
                count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
                count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
            }
        }
    });

    // 初始化表格 - 我发起的任务
    var myInitiateList = table.render({
        elem: '#myInitiateList',
        url: "/task/getMyInitiateTask",
        method: 'get',
        // toolbar: '#applyListToolBar',
        // defaultToolbar: [],
        height: 'full-150',
        cols: [
            [
                {
                    title: '操作', minWidth: 170, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">流程图</span>',
                            _modify = '<span class="layui-btn layui-btn-xs" lay-event="applyModify">修改</span>',
                            _submit = '<span class="layui-btn layui-btn-xs" lay-event="applySubmit">提交</span>';
                        if ("UNCOMMITTED" == rowData.status) {
                            _html += _modify + _submit;
                            // _html += _submit;
                        }
                        return _html;
                    }
                },
                {
                    field: 'id', hide: 'true', title: '任务ID', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData['myInitiate'].id;
                    }
                },
                {
                    field: 'processInstanceId',
                    hide: 'true',
                    title: '流程实例ID',
                    minWidth: 290,
                    align: 'center',
                    templet: rowData => {
                        return rowData['myInitiate'].processInstanceId;
                    }
                },
                {
                    field: 'type', title: '类型', minWidth: 120, align: 'center', templet: rowData => {
                        return rowData.taskType;
                    }
                },
                {
                    field: 'activityName', title: '当前节点', minWidth: 60, align: 'center', templet: rowData => {
                        return rowData['myInitiate'].name;
                    }
                },
                {
                    field: 'name', title: '审批人', minWidth: 60, align: 'center', templet: rowData => {
                        return rowData['currentNodeHandler'];
                    }
                },
                {
                    field: 'statusAudit', title: '状态', minWidth: 50, align: 'center', templet: rowData => {
                        return rowData['statusDesc'];
                    }
                },
                {
                    field: 'deploymentTime', title: '发起时间', minWidth: 160, align: 'center',
                    templet: rowData => {
                        return util.toDateString(rowData['myInitiate'].startTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    field: 'deploymentTime', title: '结束时间', minWidth: 165, align: 'center',
                    templet: rowData => {
                        let endTime = rowData['myInitiate'].endTime;
                        if(endTime){
                            return util.toDateString(endTime, 'yyyy-MM-dd HH:mm:ss');
                        }
                        return '';
                    }
                }
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
        },
        // 解决页面无数据时表头显示不全, 希望出现水平滚动条
        done: function (res, curr, count) {
            if (res.data && res.data.length == 0) {
                count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
                count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
            }
        }
    });

    /**
     *  表格头部toolbar工具栏监听事件
     */
    table.on('toolbar(todoTask)', function (obj) {
        var _assignee = $("#assigne").val();
        let _event = obj.event;
        switch (_event) {
            case 'viewApplyList':
                todoTask.reload();
                $("#assigne").val(_assignee);
                return;
            default:
                return;
        }
    });

    /**
     * 表格数据行tool操作栏监听事件
     */
    table.on('tool(historyTaskList)', function (obj) {
        let _rowData = obj.data.historicTask;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                viewProcessDiagram(_rowData.processInstanceId);
                return;
            default:
                return;
        }
    });
    table.on('tool(todoTask)', function (obj) {
        let _rowData = obj.data.taskInfo;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                viewProcessDiagram(_rowData.processInstanceId);
                return;
            case 'approve': // 审核
                _modal = layer.open({
                    type: 1,
                    area: ['550px', '250px'],
                    content: $('#approveFormLtl'),
                    success: function (index) {
                        $("#approveForm input[name='taskId']").val(_rowData.taskId);
                    }
                });
                return;
            default:
                return;
        }
    });
    table.on('tool(myInitiateList)', function (obj) {
        let _rowData = obj.data.myInitiate;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                viewProcessDiagram(_rowData.processInstanceId);
                return;
            case 'applySubmit': // 提交
                $.post("/apply/submitApply", {"processInstanceId": _rowData.processInstanceId}, function (res) {
                    console.log(res);
                    if ("0" == res.code) {
                        layer.msg("操作成功", {icon: 1, time: 1500});
                        myInitiateList.reload();
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 1500});
                    }
                }, "json");
                return;
            default:
                return;
        }
    });

    // 查看流程图
    function viewProcessDiagram(processInstanceId) {
        layer.open({
            title: '流程图',
            type: 2,
            area: ['800px', '410px'],
            content: '/holiday/processDiagram?processInstanceId=' + processInstanceId
        })
    }

    /**
     * 刷新列表
     */
    /*$(document).on('click', '#refreshApplyList', function () {
        todoTask.reload();
    });*/

    $(".refresh").click(function (e) {
        let _type = $(this).attr('id');
        switch (_type) {
            case 'refreshToDoList':
                todoTask.reload();
                return;
            case 'refreshHistoryTaskList':
                historyTaskList.reload();
                return;
            case 'refreshMyInitiateList':
                myInitiateList.reload();
                return;
            default:
                return;
        }
    });

    /**
     * 申请请假弹窗
     */
    $("#applyHoliday").click(function () {
        _modal = layer.open({
            type: 1,
            area: ['550px', '400px'],
            content: $('#applyFormLtl')
        });
    });

    /**
     * 提交申请
     */
    form.on('submit(applyFormSubmit)', function (data) {
        let _param = data.field;
        $.post("/holiday/apply", _param, function (res) {
            layer.close(_modal);
            // todoTask.reload();
            myInitiateList.reload();
        }, 'json');
        // return false; // 调试时打开，console.log才会有输出
    });

    // 审核：通过/驳回
    $("#approvePass, #approveReject").click(function (obj) {
        let approveFlag = $(this).val(),
            rejectReason = $("#approveForm textarea[name='rejectReason']").val(),
            taskId = $("#approveForm input[name='taskId']").val();
        console.log(taskId);
        if ("0" === approveFlag) {
            if (undefined == rejectReason || "" == rejectReason) {
                layer.tips('驳回原因必填', '#rejectReason', {tips: 1});
                return false;
            }
        }
        $.post("/holiday/approve", {
            "taskId": taskId,
            "approveFlag": approveFlag,
            "rejectReason": rejectReason
        }, function (res) {
            console.log(res);
        }, 'json');
    });

});
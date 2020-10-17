layui.use(['element', 'form', 'table', 'util'], function () {
    var element = layui.element,
        form = layui.form,
        table = layui.table,
        util = layui.util;

    /**
     * 初始化表格
     */
        // 待办列表
    var todoTask = table.render({
            elem: '#todoTask',
            url: "/task/getToDoTasks",
            method: 'get',
            height: 'full-110',
            cols: [
                [
                    {
                        title: '操作', minWidth: 150, align: 'center', fixed: 'left', templet: rowData => {
                            let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">查看流程图</span>',
                                _audit = '<span class="layui-btn layui-btn-xs" lay-event="approve">审核</span>',
                                _submit = '<span class="layui-btn layui-btn-xs" lay-event="reSubmit">提交</span>';
                            let _status = rowData.statusAudit;
                            if ("REJECTED" === _status) {
                                _html += _submit;
                            } else if ("UNDER_REVIEW" === _status) {
                                _html += _audit;
                            }
                            return _html;
                        }
                    },
                    {field: 'id', title: '任务ID', minWidth: 290, align: 'center'},
                    {field: 'processInstanceId', title: '流程实例ID', minWidth: 290, align: 'center'},
                    {field: 'assignee', title: '姓名', minWidth: 90, align: 'center'},
                    {field: 'activityName', title: '当前节点', minWidth: 90, align: 'center'},
                    {
                        field: 'statusAudit', title: '状态', minWidth: 90, align: 'center', templet: rowData => {
                            return rowData.statusPairValue[rowData.statusAudit];
                        }
                    },
                    {
                        field: 'deploymentTime',
                        title: '申请时间',
                        minWidth: 160,
                        align: 'center',
                        templet: rowData => {
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
            },
            // 解决页面无数据时表头显示不全, 希望出现水平滚动条
            done: function (res, curr, count) {
                count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
                count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
            }
        });
    // 已办列表
    var historyTaskList = table.render({
        elem: '#historyTaskList',
        url: "/task/getHistoryTask",
        method: 'get',
        height: 'full-110',
        cols: [
            [
                {
                    title: '操作', minWidth: 150, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">查看流程图</span>',
                            _audit = '<span class="layui-btn layui-btn-xs" lay-event="approve">审核</span>',
                            _submit = '<span class="layui-btn layui-btn-xs" lay-event="reSubmit">提交</span>';
                        let _status = rowData.statusAudit;
                        if ("REJECTED" === _status) {
                            _html += _submit;
                        } else if ("UNDER_REVIEW" === _status) {
                            _html += _audit;
                        }
                        return _html;
                    }
                },
                {
                    title: '任务ID', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData.taskInfo.id;
                    }
                },
                {
                    title: '流程实例ID', minWidth: 290, align: 'center', templet: rowData => {
                        return rowData.taskInfo.processInstanceId;
                    }
                },
                {
                    title: '姓名', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.taskInfo.assignee;
                    }
                },
                {
                    title: '名称', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.taskInfo.activityName;
                    }
                },
                {
                    field: 'statusAudit', title: '状态', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.statusPairValue[rowData.statusAudit];
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
            count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
            count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
        }
    });
    // 我发起的任务
    var myInitiateList = table.render({
        elem: '#myInitiateList',
        url: "/task/getMyInitiateTask",
        method: 'get',
        // toolbar: '#applyListToolBar',
        // defaultToolbar: [],
        height: 'full-110',
        cols: [
            [
                {
                    title: '操作', minWidth: 150, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">流程图</span>',
                            _audit = '<span class="layui-btn layui-btn-xs" lay-event="approve">审核</span>',
                            _submit = '<span class="layui-btn layui-btn-xs" lay-event="reSubmit">提交</span>';
                        let _status = rowData.statusAudit;
                        if ("REJECTED" === _status) {
                            _html += _submit;
                        } else if ("UNDER_REVIEW" === _status) {
                            _html += _audit;
                        }
                        return _html;
                    }
                },
                {field: 'id', title: '任务ID', minWidth: 290, align: 'center'},
                {field: 'processInstanceId', title: '流程实例ID', minWidth: 290, align: 'center'},
                {field: 'assignee', title: '姓名', minWidth: 90, align: 'center'},
                {field: 'activityName', title: '当前节点', minWidth: 90, align: 'center'},
                {
                    field: 'statusAudit', title: '状态', minWidth: 90, align: 'center', templet: rowData => {
                        return rowData.statusPairValue[rowData.statusAudit];
                    }
                },
                {
                    field: 'deploymentTime', title: '发起时间', minWidth: 160, align: 'center',
                    templet: rowData => {
                        return util.toDateString(rowData.createTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    field: 'deploymentTime', title: '结束时间', minWidth: 160, align: 'center',
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
            count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
            count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
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
                todoTask.reload(getTableConfig(_assignee));
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
        let _rowData = obj.data;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                layer.open({
                    title: '流程图',
                    type: 2,
                    area: ['800px', '410px'],
                    content: '/holiday/processDiagram?processInstanceId=' + _rowData.processInstanceId
                })
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
    table.on('tool(todoTask)', function (obj) {
        let _rowData = obj.data;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                layer.open({
                    title: '流程图',
                    type: 2,
                    area: ['800px', '410px'],
                    content: '/holiday/processDiagram?processInstanceId=' + _rowData.processInstanceId
                })
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
        let _rowData = obj.data;
        switch (obj.event) {
            case 'viewProcessDiagram': // 查看流程图
                layer.open({
                    title: '流程图',
                    type: 2,
                    area: ['800px', '410px'],
                    content: '/holiday/processDiagram?processInstanceId=' + _rowData.processInstanceId
                })
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

    /**
     * 刷新列表
     */
    /*$(document).on('click', '#refreshApplyList', function () {
        todoTask.reload();
    });*/

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
            todoTask.reload();
        }, 'json');
        // return false; // 调试时打开，console.log才会有输出
    });

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
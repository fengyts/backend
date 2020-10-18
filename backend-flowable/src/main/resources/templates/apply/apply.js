layui.use(['form', 'table', 'util'], function () {
    var form = layui.form,
        table = layui.table,
        util = layui.util;
    var _modal; // 模态框弹窗

    /**
     * 申请请假弹窗
     */
    $("#applyHoliday").click(function () {
        _modal = layer.open({
            type: 1,
            title: '请假表单--[' + sysUser.realName + ']',
            area: ['550px', '410px'],
            content: $('#applyFormLtl')
        });
    });

    $("#applyExpense").click(function () {
        layer.tips("功能未开发!", "#applyExpense", {tips: 2, time: 1500});
    });

    /**
     * 申请-保存
     */
    form.on('submit(applyFormSave)', function (data) {
        let _param = data.field;
        // console.log(_param);
        // return false; // 调试时打开，console.log才会有输出
        $.post("/apply/saveApply", _param, function (res) {
            if ("0" == res.code) {
                layer.msg("操作成功", {icon: 1, time: 1500});
            } else {
                layer.msg(res.msg, {icon: 2, time: 1500});
            }
            layer.close(_modal);
        }, 'json');
    });

    /**
     * 申请-提交
     */
    form.on('submit(applyFormSubmit)', function (data) {
        let _param = data.field;
        // console.log(_param);
        // return false; // 调试时打开，console.log才会有输出
        $.post("/apply/submitApply", _param, function (res) {
            if ("0" == res.code) {
                layer.msg("操作成功", {icon: 1, time: 1500});
            } else {
                layer.msg(res.msg, {icon: 2, time: 1500});
            }
            layer.close(_modal);
        }, 'json');
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
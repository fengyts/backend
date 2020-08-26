$(function () {

    formRequired();
    formEditReadonly();

    // 设置表单必填项 ‘*’ 样式
    function formRequired() {
        let _formRequired = '<span style="color: red;font-size:16px;vertical-align:middle;">*</span>';
        $(".form-required").append(_formRequired);
    }

    // 设置表单编辑页 ‘数据不可修改’ 样式
    function formEditReadonly() {
        $(".form-readonly").addClass('layui-disabled');
    }

});

// 关闭layer弹窗函数
function closePage(index) {
    if (index) {
        parent.layer.close(index);
    } else {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    }
}

// form 简单表单 数据获取，json对象格式
function serializeObj(formId) {
    var formJson = {};
    var formArray = $("#" + formId).serializeArray();
    $.each(formArray, function (i, n) {
        formJson[n.name] = n.value;
    })
    return formJson;
}

// form 简单表单 数据获取，json对象格式的字符串
function serializeObjStr(formId) {
    return JSON.stringify(serializeObj(formId));
}
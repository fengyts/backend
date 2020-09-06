$(function () {

    formRequired();
    formEditReadonly();
    closeLayerOpen();

    // 设置表单必填项 ‘*’ 样式
    function formRequired() {
        let _formRequired = '<span style="color: red;font-size:16px;vertical-align:middle;">*</span>';
        $(".form-required").append(_formRequired);
    }

    // 设置表单编辑页 ‘数据不可修改’ 样式
    function formEditReadonly() {
        $(".form-readonly").addClass('layui-disabled');
    }

    // layer弹窗的取消按钮 - 关闭layer弹窗
    function closeLayerOpen(index){
        $('.cancelBtn').on('click', function (w) {
            closePage();
        });
    }

});

function layuiSwitch(filter){
    filter = filter ? filter : 'switchStatu';
    // 监听switch事件, 处理switch切换时赋值和提交获取值
    form.on('switch(' + filter + ')', function (obj) {
        // var c = obj.elem.checked; // 获取开关状态
        // $(this).val(c ? 1 : 0);
        $(this).attr('type', 'hidden').val(this.checked ? 1 : 0);
    });
}

var layConfig = {};

function openWindow(layConfig){
    var _type = layConfig.type,
        _title = layConfig.title,
        _area = layConfig.area,
        _url = layConfig.url,
        _scrollbar = layConfig.scrollbar
    ;
    _type = _type ? _type : 2;
    _area = _area ? _area : ['770px', '450px'];
    // 这里scrollbar要设置为true, 解决关闭弹窗时父页面因为垂直滚动条宽度导致页面抖动
    _scrollbar = _scrollbar ? _scrollbar : false;
    let index = layer.open({
        type: _type ? _type : 2,
        title: _title,
        // skin: 'layui-layer-molv',
        border: [3, 0.3, '#000'],
        shadeClose: false,
        shade: [0.3, '#000'],
        maxmin: true,
        scrollbar: _scrollbar,
        area: _area,
        content: _url
    });
    return index;
}

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
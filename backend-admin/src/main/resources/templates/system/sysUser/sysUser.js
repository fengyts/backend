layui.use(['table', 'form', 'layer'], function () {
    var table = layui.table,
        form = layui.form,
        layer = layui.layer;

    /**列表初始化**/
    var tableList = table.render({
        elem: '#list',
        // url: contextPath + "merger/queryRecord",
        url: "/system/sysUser/queryPageList",
        method: 'post',
        cellMinWidth: 80,
        height: 'full-130',
        cols: [
            [
                // {type: 'radio', width: 30, fixed: 'left'},
                {field: 'id', hide: true},
                {field: 'userName', title: '用户账号', minWidth: 100, align: 'center'},
                {field: 'nickName', title: '用户名称', minWidth: 100, align: 'center'},
                {field: 'mobile', title: '手机号', width: 120, align: 'center'},
                {field: 'email', title: '邮箱', minWidth: 150, align: 'center'},
                {
                    field: 'sex', title: '性别', align: 'center', templet: rowData => {
                        let options = {"0": "女", "1": "男", "2": "未知"};
                        return options[rowData.sex];
                    }
                },
                {
                    field: 'status', title: '状态', align: 'center', templet: rowData => {
                        let status = rowData.status;
                        let options = {"0": "冻结", "1": "正常"};
                        return status != 'undefined' ? options[status] : "";
                    }
                },
                // {
                //     title: '操作', width: 80, align: 'center', fixed: 'right',
                //     toolbar:'<script type="text/html"><span class="layui-btn layui-badge layui-bg-green" lay-event="edit">编辑</span></script>'
                // },
                {
                    title: '操作', width: 80, align: 'center', fixed: 'right', templet: function (rowData) {
                        var html;
                        if ('superadmin' === rowData.userName) { // superadmin数据不可被编辑
                            html = '<span class="layui-btn layui-btn-xs layui-btn-disabled">编辑</span>';
                            // return '超级管理员不能修改';
                        } else {
                            html = '<span class="layui-btn layui-badge layui-bg-green" lay-event="edit">编辑</span>';
                        }
                        return html;
                    }
                }

            ]
        ],
        page: {
            // first: '首页',
            // last: '尾页',
            layout: ['refresh', 'prev', 'page', 'next', 'skip', 'limit', 'count'],
            limits: [10, 20, 30, 50, 100]
        },
        parseData: function (res) {
            // console.log(res);
            return {
                "code": '',
                // "msg": res.msg,
                "count": res.total,
                "data": res.records
            };
        },
        response: {
            statusCode: ''
        },
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'rows'
        }
    });

    $("#add").on('click', function () {
        layer.open({
            type: 2,
            title: '用户管理 >> 新增',
            // skin: 'layui-layer-molv',
            border: [3, 0.3, '#000'],
            shadeClose: false,
            shade: [0.3, '#000'],
            maxmin: true,
            scrollbar: false,
            area: ['770px', '420px'],
            content: '/system/sysUser/add'
        });
    });

    // 弹窗取消按钮 - 关闭弹窗
    $(".cancelBtn").on('click', function () {
        closePage();
    });

    // 保存
    $(".saveBtn").on('click', function () {
        form.on('submit(formSubmit)', function (data) {
            let formData = JSON.stringify(data.field);
            // let formData = serializeObjStr('formId');
            $.ajax({
                url: '/system/sysUser/saveOrUpdate',
                type: 'POST',
                data: formData,
                contentType: 'application/json;charset=UTF-8',
                dataType: 'json',
                success: function (res) {
                    if ('0' == res.code) {
                        parent.window.location.reload();
                        closePage();
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 1500});
                    }
                },
                error: function (res) {
                    layer.msg('系统繁忙', {icon: 2, time: 1500});
                }
            });
        });
    });

    // 监听编辑事件
    table.on('tool(list)', function (obj) {
        let rowData = obj.data;
        switch (obj.event) {
            case 'edit':
                edit(rowData.id);
                break;
            default:
                return;
        }
    });

    // 监听switch事件, 处理switch切换时赋值和提交获取值
    form.on('switch(switchStatu)', function (obj) {
        // var c = obj.elem.checked; // 获取开关状态
        // $(this).val(c ? 1 : 0);
        $(this).attr('type', 'hidden').val(this.checked ? 1 : 0);
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '用户管理 >> 编辑',
            // skin: 'layui-layer-molv',
            border: [3, 0.3, '#000'],
            shadeClose: false,
            shade: [0.3, '#000'],
            maxmin: true,
            scrollbar: false,
            area: ['770px', '420px'],
            content: '/system/sysUser/edit?userId=' + id
        });
    }

});
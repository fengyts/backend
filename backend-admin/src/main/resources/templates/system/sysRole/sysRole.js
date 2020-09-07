layui.use(['table', 'form', 'layer'], function () {
    var table = layui.table,
        form = layui.form,
        layer = layui.layer;

    /**列表初始化**/
    var tableList = table.render({
        elem: '#list',
        url: "/system/sysRole/getAllRoles",
        method: 'get',
        // cellMinWidth: 80,
        // height: 'full-80',
        toolbar: '<div><span style="height: 10px; color: indianred; font-size: 12px;">(点击角色名称获取菜单权限)</span></div>',
        defaultToolbar: [],
        cols: [
            [
                // {type: 'radio', width: 30, fixed: 'left'},
                {field: 'id', hide: true},
                {
                    field: 'roleName', title: '角色名称', minWidth: 100, align: 'center', templet: rowData => {
                        let _a = '<a href="javascript:void(0);" class="layui-table-link" mid="' + rowData.id + '" ' +
                            'lay-event="associate">' + rowData.roleName + '</a>';
                        return _a;
                    }
                },
                {field: 'description', title: '描述', width: 120, align: 'center'},
                {
                    field: 'status', title: '状态', align: 'center', templet: rowData => {
                        let _switch = (val, _c) => {
                            let _iss = '<input type="checkbox" ' + _c + ' name="status" lay-skin="switch" ' +
                                'lay-filter="switchStatu" lay-text="启用|禁用" value="' + val + '" mid="' + rowData.id + '">';
                            return _iss;
                        }
                        return 1 === rowData.status ? _switch(1, 'checked') : _switch(0);
                    }
                },
                {
                    title: '操作', width: 80, align: 'center', fixed: 'right', templet: function (rowData) {
                        var html;
                        if ('superAdmin' === rowData.roleCode) { // superadmin数据不可被编辑
                            html = '<span class="layui-btn layui-btn-xs layui-btn-disabled">编辑</span>';
                        } else {
                            html = '<span class="layui-btn layui-badge layui-bg-green" lay-event="edit">编辑</span>';
                        }
                        return html;
                    }
                }
            ]
        ],
        page: false,
        parseData: function (res) {
            return {
                "code": '',
                "data": res.data
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

    // 监听表单的 switch事件, 处理switch切换时赋值和提交获取值
    layuiSwitch('switchStatu');

    //监听list列表页面的 switch开关
    form.on('switch(switchStatu)', function (data) {
        let _id = $(this).attr('mid'),
            x = this.checked,
            _val = this.value,
            _msg;
        if (x) {
            _msg = "确认启用?";
            _val = 1;
        } else {
            _msg = "确认禁用?";
            _val = 0;
        }
        layer.confirm(
            _msg,
            {icon: 3},
            function (index) { //确定按钮
                let _d = {"id": _id, "status": _val};
                let _data = JSON.stringify(_d);
                $.ajax({
                    url: '/system/sysRole/saveOrUpdate',
                    type: 'POST',
                    data: _data,
                    contentType: 'application/json;charset=UTF-8',
                    dataType: 'json',
                    success: function (res) {
                        if ('0' == res.code) {
                            // parent.window.location.reload();
                            // closePage();
                            table.render();
                            layer.close(index);
                        } else {
                            table.render();
                            layer.msg(res.msg, {icon: 2, time: 1500});
                        }
                    },
                    error: function (res) {
                        layer.msg('系统繁忙', {icon: 2, time: 1500});
                    }
                });
            },
            function (index) { //取消按钮
                data.elem.checked = !x;
                form.render();
                layer.close(index);
            }
        );
    });

    $("#add").click(function () {
        openPage('', 'add');
    });

    // 监听编辑事件
    table.on('tool(list)', function (obj) {
        let rowData = obj.data;
        switch (obj.event) {
            case 'edit': // 编辑事件
                openPage(rowData.id, obj.event);
                return;
            case 'associate': // 角色关联菜单权限事件
                associate(rowData.id);
                return;
            default:
                return;
        }
    });

    // 右边角色关联菜单权限栏目
    function associate(id){
        $("#role_menu_associate").append(id).append("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
    }

    function openPage(id, _eve) {
        let _title, _url;
        if ('add' === _eve) {
            _title = "系统角色 >> 新增";
            _url = "/system/sysRole/add";
        } else {
            _title = "系统角色 >> 编辑";
            _url = "/system/sysRole/edit?id=" + id;
        }
        var config = {
            "title": _title,
            "area": ['600px', '400px'],
            "url": _url
        };
        openWindow(config);
    }

    // 保存
    form.on('submit(formSubmit)', function (data) {
        let formData = JSON.stringify(data.field);
        $.ajax({
            url: '/system/sysRole/saveOrUpdate',
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
$(function (d) {
    layui.extend({
        treeTable: '/plugins/layui-treetable/treeTable'
    }).use(['treeTable', 'layer', 'code', 'form'], function () {
        var o = layui.$,
            form = layui.form,
            layer = layui.layer,
            treeTable = layui.treeTable;
        var re = treeTable.render({
            elem: '#tree-table',
            url: '/system/sysMenu/getMenuList',
            icon_key: 'menuName',
            // is_cache: false,
            // is_checkbox: false,
            primary_key: 'id',
            parent_key: 'parentId',
            checked: {
                key: 'id',
                // data: [0, 1, 4, 10, 11, 5, 2, 6, 7, 3, 8, 9],
            },
            end: function (e) {
                form.render();
            },
            cols: [
                {
                    key: 'menuName', title: '名称', width: '150px', template: function (item) {
                        let level = item.level;
                        let html = (style) => {
                            return '<span style="' + style + '">' + item.menuName + '</span>';
                        }
                        if (2 === item.type)
                            return html("color: #40AFFE;"); // 按钮单独显示样式
                        // return '<span style="color: #40AFFE;">' + item.menuName + '</span>';
                        switch (level) {
                            case 0 :
                                return html("color:green;");
                            case 1:
                                return html("color:green;");
                            case 2:
                                return html("color: mediumseagreen;");
                            default:
                                return html("color: mediumseagreen;");
                        }
                    }
                },
                /*{
                    key: 'id', title: 'ID', width: '10px', align: 'center'
                },
                {
                    key: 'parentId', title: '父ID', width: '10px', align: 'center',
                },*/
                {
                    key: 'url', title: 'url', width: '80px', align: 'center', template: function (item) {
                        let _url = item.url;
                        return _url === undefined ? '' : _url;
                    }
                },
                {
                    key: 'type', title: '类型', width: '50px', align: 'center', template: function (item) {
                        let menuTypes = {
                            0: {"desc": "目录", "style": "color: green;"},
                            1: {"desc": "菜单", "style": "color: mediumseagreen;"},
                            2: {"desc": "按钮", "style": "color: #40AFFE;"}
                        };
                        // return menuTypes[item.type];
                        let _span = '<span style="' + menuTypes[item.type].style + '">' + menuTypes[item.type].desc + '</span>';
                        return _span;
                    }
                },
                {
                    key: 'sort', title: '排序', width: '20px', align: 'center'
                },
                {
                    key: 'icon', title: '图标', width: '50px', align: 'center', template: function (item) {
                        return item.icon === undefined ? '' : item.icon;
                    }
                },
                {
                    title: '显示状态', width: '50px', align: 'center', template: function (item) {
                        let _switch = (val, _c) => {
                            let _iss = '<input type="checkbox" ' + _c + ' name="isShow" lay-skin="switch" ' +
                                'lay-filter="switchShow" lay-text="显示|隐藏" value="' + val + '" mid="' + item.id + '">';
                            return _iss;
                        }
                        if (1 === item.isShow)
                            return _switch(1, 'checked');
                        else
                            return _switch(0);
                    }
                },
                {
                    title: '操作', align: 'center', width: '50px', template: function (item) {
                        let _add = '<a class="layui-btn layui-btn-xs" lay-filter="add">添加</a>',
                            _edit = '<a class="layui-btn layui-btn-xs" lay-filter="edit">编辑</a>';
                        if (2 === item.type) return _edit; // 按钮只能编辑,不可新增子菜单
                        return _add + _edit;
                    }
                }
            ]
        });
        /*
        // 监听展开关闭
        treeTable.on('tree(flex)', function (data) {
            layer.msg(JSON.stringify(data));
        })*/
        /*
        // 监听checkbox选择
        treeTable.on('tree(box)', function (data) {
            if (o(data.elem).parents('#tree-table1').length) {
                var text = [];
                o(data.elem).parents('#tree-table1').find('.cbx.layui-form-checked').each(function () {
                    o(this).parents('[data-pid]').length && text.push(o(this).parents('td').next().find('span').text());
                })
                o(data.elem).parents('#tree-table1').prev().find('input').val(text.join(','));
            }
            layer.msg(JSON.stringify(data));
        })*/
        /*
        // 获取选中值，返回值是一个数组（定义的primary_key参数集合）
        o('.get-checked').click(function () {
            layer.msg('选中参数' + treeTable.checked(re).join(','))
        })*/
        /*
        // 随机更换小图标
        o('.change-icon').click(function () {
            var arr = [
                {
                    open: 'layui-icon layui-icon-set',
                    close: 'layui-icon layui-icon-set-fill',
                    left: 16,
                },
                {
                    open: 'layui-icon layui-icon-rate',
                    close: 'layui-icon layui-icon-rate-solid',
                    left: 16,
                },
                {
                    open: 'layui-icon layui-icon-tread',
                    close: 'layui-icon layui-icon-praise',
                    left: 16,
                },
                {
                    open: 'layui-icon layui-icon-camera',
                    close: 'layui-icon layui-icon-camera-fill',
                    left: 16,
                },
                {
                    open: 'layui-icon layui-icon-user',
                    close: 'layui-icon layui-icon-group',
                    left: 16,
                },
            ];
            var round = Math.round(Math.random() * (arr.length - 1));
            re.icon = arr[round];
            treeTable.render(re);
        });*/

        // 刷新重载树表（一般在异步处理数据后刷新显示）
        o('.refresh').click(function () {
            treeTable.render();
            // form.render();
            /*re.data.push({"id": 12, "parentId": 1, "menuName": "1-4", "level": 2}, {"id": 13, "parentId": 2, "menuName": "1-4-1", "level": 3});
            treeTable.render(re);*/
        })
        // 全部展开
        o('.open-all').click(function () {
            treeTable.openAll(re);
        })
        // 全部关闭
        o('.close-all').click(function () {
            treeTable.closeAll(re);
        })

        // 新增根菜单(目录)
        o('.addRootMenu').click(function () {
            let id = 0; // 根目录菜单父id固定为0
            openPage(id, "add");
        });
        // 监听自定义 -> 新增
        treeTable.on('tree(add)', function (data) {
            // layer.msg(JSON.stringify(data));
            let id = data.item.id;
            openPage(id, "add");
        })
        // 监听自定义 -> 编辑
        treeTable.on('tree(edit)', function (data) {
            // layer.msg(JSON.stringify(data));
            let id = data.item.id;
            openPage(id, "edit");
        })

        // 监听表单的 switch事件, 处理switch切换时赋值和提交获取值
        layuiSwitch('switchStatu');

        //监听list列表页面的 switch开关
        form.on('switch(switchShow)', function (data) {
            let _id = o(this).attr('mid'),
                x = this.checked,
                _val = this.value,
                _msg;
            if (x) {
                _msg = "确认显示?";
                _val = 1;
            } else {
                _msg = "确认隐藏?";
                _val = 0;
            }
            layer.confirm(
                _msg,
                {icon: 3},
                function (index) {//确定按钮
                    $.post('/system/sysMenu/update', {"id": _id, "isShow": _val}, function (res) {
                        form.render();
                        layer.close(index);
                    })
                },
                function (index) { //取消按钮
                    data.elem.checked = !x;
                    form.render();
                    layer.close(index);
                }
            );
            /*layer.open({
                content: 'test'
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    data.elem.checked = x;
                    form.render();
                    layer.close(index);
                    //按钮【按钮一】的回调
                }
                , btn2: function (index, layero) {
                    //按钮【按钮二】的回调
                    data.elem.checked = !x;
                    form.render();
                    layer.close(index);
                    //return false 开启该代码可禁止点击该按钮关闭
                }
                , cancel: function () {
                    //右上角关闭回调
                    data.elem.checked = !x;
                    form.render();
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
            return false;*/
        });

        o('.saveBtn').on('click', function (e) {
            form.on('submit(formSubmit)', function (data) {
                let formData = JSON.stringify(data.field);
                $.ajax({
                    url: '/system/sysMenu/saveOrUpdate',
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

    });

    function openPage(id, _eve) {
        let _title, _url;
        if ('add' === _eve) {
            _title = "菜单 >> 新增";
            _url = "/system/sysMenu/add?id=" + id;
        } else {
            _title = "菜单 >> 编辑";
            _url = "/system/sysMenu/edit?id=" + id;
        }
        var config = {
            "title": _title,
            "area": ['770px', '430px'],
            "url": _url
        };
        openWindow(config);
    }


});
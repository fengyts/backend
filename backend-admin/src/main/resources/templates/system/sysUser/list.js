layui.use(['table', 'form'], function () {
    var table = layui.table,
        form = layui.form;

    /**列表初始化**/
    var tableList = table.render({
        elem: '#list',
        // url: contextPath + "merger/queryRecord",
        url: "/system/sysUser/queryPageList",
        method: 'post',
        cellMinWidth: 100,
        height: 'full-90',
        cols: [
            [
                {type: 'radio', width: 30, fixed: 'left'},
                {field: 'nickName', title: '用户名称', align: 'center'},
                {field: 'mobile', title: '手机号', align: 'center'},
                {field: 'email', title: '邮箱', align: 'center'},
                {field: 'sex', title: '性别', align: 'center', templet: rowData => {
                        let options = {"0": "女", "1": "男", "2": "未知"};
                        return options[rowData.sex];
                    }
                },
                {field: 'status', title: '状态', align: 'center'},
                {field: 'status', title: '状态', align: 'center'},
                {field: 'status', title: '状态', align: 'center'},
                {field: 'status', title: '状态', align: 'center'},
                {field: 'status', title: '状态', align: 'center'},
                {field: 'status', title: '状态l', align: 'center'},
                {title: '操作', width: 100, align: 'center', fixed:'right', templet: function (rowData) {
                        var html = '<span class="layui-badge layui-bg-green" lay-event="view">查看</span>';
                        return html;
                    }
                }

            ]
        ],
        page: {
            layout: ['refresh', 'skip', 'prev', 'page', 'next', 'limit', 'count'],
            limits: [10, 20, 30, 50, 100]
        },
        parseData: function (res) {
            // console.log(res);
            return {
                "code": '',
                // "msg": res.message,
                "count": res.total,
                "data": res.records
            };
        },
        response: {
            statusCode: ''
        },
        request: {
            limitName: 'rows'
        }
    });

});
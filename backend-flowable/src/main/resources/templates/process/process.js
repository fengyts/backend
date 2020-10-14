layui.use(['table', 'util'], function () {
    var table = layui.table, util = layui.util;

    var deploymentList = table.render({
        elem: '#deploymentList',
        url: "/process/getDeploymentListData",
        method: 'get',
        toolbar: '#deploymentListToolBar',
        defaultToolbar: [],
        // height: 'full-180',
        cols: [
            [
                {field: 'id', title: 'id', minWidth: 410, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {
                    field: 'deploymentTime', title: '发布时间', minWidth: 300, align: 'center', templet: rowData => {
                        return util.toDateString(rowData.deploymentTime, 'yyyy-MM-dd HH:mm:ss');
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
    var definitionList = table.render({
        elem: '#definitionList',
        url: "/process/getProcessDefinitionListData",
        method: 'get',
        toolbar: '#definitionListToolBar',
        defaultToolbar: [],
        // height: 'full-180',
        cols: [
            [
                {
                    title: '操作', minWidth: 200, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">查看流程图</span>',
                            _deployment = '<span class="layui-btn layui-btn-xs" lay-event="deployment">部署</span>',
                            _edit = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessXml">查看XML</span>';
                        return _html + _deployment + _edit;
                    }
                },
                {field: 'id', title: 'id', minWidth: 410, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {field: 'key', title: '流程Key', minWidth: 150, align: 'center'},
                {field: 'version', title: '版本', minWidth: 30, align: 'center'},
                {field: 'deploymentId', title: '部署ID', minWidth: 300, align: 'center'},
                {field: 'description', title: '描述', minWidth: 150, align: 'center'},
                {field: 'resourceName', title: '来源', minWidth: 550, align: 'center'}
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

    $(document).on('click', '#refreshDeploymentList', function () {
        deploymentList.reload();
    });
    $(document).on('click', '#refreshProcessDefinition', function () {
        definitionList.reload();
    });
});
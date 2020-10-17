layui.use(['table', 'util', 'upload'], function () {
    var table = layui.table,
        util = layui.util,
        upload = layui.upload,
        _modal; // 模态框

    var deploymentList = table.render({
        elem: '#deploymentList',
        url: "/process/getDeploymentListData",
        method: 'get',
        toolbar: '#deploymentListToolBar',
        defaultToolbar: [],
        // height: 'full-180',
        cols: [
            [
                {field: 'id', title: '部署ID', minWidth: 300, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {
                    field: 'deploymentTime', title: '部署时间', minWidth: 300, align: 'center', templet: rowData => {
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
        },
        // 解决页面无数据时表头显示不全, 希望出现水平滚动条
        done: function (res, curr, count) {
            count || this.elem.next('.layui-table-view').find('.layui-table-header').css('display', 'inline-block');
            count || this.elem.next('.layui-table-view').find('.layui-table-box').css('overflow', 'auto');
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
                    title: '操作', minWidth: 220, align: 'center', fixed: 'left', templet: rowData => {
                        let _html = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessDiagram">查看流程图</span>',
                            _deployment = '<span class="layui-btn layui-btn-xs" lay-event="deployment">部署</span>',
                            _edit = '<span class="layui-btn layui-btn-xs" lay-event="viewProcessXml">查看XML</span>';
                        return _html + _deployment + _edit;
                    }
                },
                // {field: 'id', title: 'id', minWidth: 410, align: 'center'},
                {field: 'name', title: '名称', minWidth: 150, align: 'center'},
                {field: 'key', title: 'Key', minWidth: 150, align: 'center'},
                {field: 'version', title: '版本', minWidth: 30, align: 'center'},
                {field: 'deploymentId', title: '部署ID', minWidth: 300, align: 'center'},
                {field: 'description', title: '描述', minWidth: 150, align: 'center'},
                // {field: 'resourceName', title: '来源', minWidth: 550, align: 'center'}
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

    // 列表刷新操作
    $(document).on('click', '#refreshDeploymentList', function () {
        deploymentList.reload();
    });
    $(document).on('click', '#refreshProcessDefinition', function () {
        definitionList.reload();
    });

    $("#importProcessModel").click(function () {
        _modal = layer.open({
            type: 1,
            area: ['500px', '200px'],
            content: $('#uploadProcessModelFormLtl')
        });
    });

    //选完文件后不自动上传
    upload.render({
        elem: '#selectUploadFile'
        , url: '/process/importProcessModel' //改成您自己的上传接口
        , auto: false
        //,multiple: true
        , bindAction: '#doUploadFile'
        , accept: 'file'
        // ,acceptMime: 'image/png,image/x-ms-bmp,application/xml,text/plain'
        , exts: 'bpmn|bpmn20.xml'
        , done: function (res, index, upload) {
            layer.msg('上传成功');
            console.log(res)
            if ("0" == res.code) {
                layer.msg("上传成功", {icon: 1, time: 1500}, function(){
                    layer.close(_modal);
                });
            } else {
                layer.msg("上传失败, 服务器异常", {icon: 2, time: 1500}, function(){
                    layer.close(_modal);
                });
            }
        }
        , error: function (index, upload) {
            layer.msg("上传失败，请重新上传", {icon: 2, time: 1500})
            layer.close(_modal);
        }
    });

});
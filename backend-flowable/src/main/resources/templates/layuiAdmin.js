$(function () {

    var element;

    // loadMenus(); // 加载菜单
    loadStaticMenus(); // 加载模拟菜单数据

    // tab操作
    layui.use('element', function () {
        element = layui.element;
        element.render('nav', 'nav-menus');
        element.on('nav(nav-menus)', function (elem) {
            var _title = $(this).text();
            var _content = $(this).attr('lay-href');
            var _exist = $("li[lay-id='" + _content + "']").length; //判断是否存在tab
            if (_content && _content != undefined && _exist == 0) { // tab是否已经打开
                var _contentTxt = '<iframe class="layadmin-iframe content-iframe" src="' + _content + '" ' +
                    'width="100%" height="100%" frameborder=0 scrolling="auto"></iframe>';
                element.tabAdd('tabCard', {
                    title: _title
                    , content: _contentTxt //支持传入html
                    , id: _content
                });
            }
            element.tabChange('tabCard', _content); // 切换tab
            fixedHome();
        });
        element.render('nav', 'nav-menus');
        fixedHome();
    });

    $(document).on('click', '.layui-tab-close', function () {
        setTimeout(function() {
            fixedHome();
        }, 50)
    });

    function fixedHome() {
        $("#tab_home0 .layui-tab-close").remove();
    }

    function loadMenus() {
        $.get("/getMenus", function (data) {
            initMenus(data);
        }, 'json');
    }

    function loadStaticMenus(){
        initStaticMenus();
    }

    function initStaticMenus() {
        var _ul = $('<ul class="layui-nav layui-nav-tree" lay-filter="nav-menus" id="nav_left_' + 1 + '"></ul>');

        var _li1 = $('<li class="layui-nav-item layui-nav-itemed"><a class="" href="javascript:;">流程管理</a>');
        var _dl1 = '<dl class="layui-nav-child">' +
            '<dd><a href="javascript:;" lay-href="process/processList">流程管理</a></dd>' +
            '<dd><a href="javascript:;" lay-href="holidayTask/list">任务列表</a></dd>' +
            '<dd><a href="javascript:;" lay-href="holiday/applyList">我发起的申请</a></dd>' +
            // '<dd><a href="javascript:;" lay-href="/holiday/auditList">待我审批的申请</a></dd>' +
            '</dl>';
        $(_li1).append(_dl1);
        $(_ul).append(_li1);

        // var _li2 = $('<li class="layui-nav-item layui-nav-itemed"><a class="" href="javascript:;">菜单二</a>');
        // var _dl2 = '<dl class="layui-nav-child">' +
        //     '<dd><a href="javascript:;" lay-href="/view/test1">菜单2一</a></dd>' +
        //     '<dd><a href="javascript:;" lay-href="/view/test2">菜单2二</a></dd>' +
        //     '</dl>';
        // $(_li2).append(_dl2);
        // $(_ul).append(_li2);

        $("#nav_left_p").append(_ul);
    }

    function initMenus(data) {
        if (data === null || !data) return;
        $.each(data, function (i, n) {
            var _menus = n.subMenus;
            if (!_menus || _menus.length < 1) return; // 沒有子菜单不显示
            // 初始化水平导航
            var _navLeftId = "nav_left_" + n.id;
            var _tli = $('<li class="layui-nav-item" navleftid="' + _navLeftId + '"><a href="javascript:;">' + n.menuName + '</a></li>');
            $("#nav_top").append(_tli);
            // top menu 点击事件
            _tli.click(function () {
                let _nli = "#" + _navLeftId;
                $("#nav_left_p ul").addClass('layui-hide');
                $(_nli).removeClass('layui-hide');
            });
            // 初始化左边导航
            var _ul;
            if (i == 0) { // 显示第一个
                _ul = $('<ul class="layui-nav layui-nav-tree layui-hide-us0" lay-shrink="all" lay-filter="nav-menus" id="' + _navLeftId + '"></ul>');
            } else {
                _ul = $('<ul class="layui-nav layui-nav-tree layui-hide" lay-shrink="all" lay-filter="nav-menus" id="' + _navLeftId + '"></ul>');
            }
            $.each(_menus, function (il, nl) {
                var _li = $('<li class="layui-nav-item layui-nav-itemed"><a class="" href="javascript:;">' + nl.menuName + '</a>');
                var _subs = nl.subMenus;
                $.each(_subs, function (ils, nls) {
                    var _dl = $('<dl class="layui-nav-child"></dl>');
                    var _dd = $('<dd><a href="javascript:;" lay-href="' + nls.url + '">' + nls.menuName + '</a></dd>');
                    $(_dl).append(_dd);
                    $(_li).append(_dl);
                });
                $(_ul).append(_li);
            });
            $("#nav_left_p").append(_ul);
        });
    }

});


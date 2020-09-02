layui.use('element', function () {
    var element = layui.element;
    // 记录当前显示的页签和iframe
    var $tab_show_id = '#tab-home', $frame_show_id = '#frame-home';

    loadMenus(); // 加载菜单
    openTab(); // 页签打开 | 页签切换
    operateHomeTab(); // 首页标签点击
    navTabHandle('#tab-handle-box'); // 标签页样式
    operateTabHandle(); // 标签页点击操作
    iframeSelfAdaption(); // iframe 自适应高度设置: 当浏览器放大缩小时的自适应设置

    // iframe 自适应高度设置: 当浏览器放大缩小时的自适应设置
    function iframeSelfAdaption() {
        window.onresize = function () {
            changeFrameHeight($frame_show_id.substr(1));
        }
    }

    // 标签页点击操作
    function operateTabHandle() {
        layui.use('util', function () {
            var util = layui.util;
            util.event('lay-active', {
                'refresh-current': function () { // 刷新本页
                    $($frame_show_id).attr('src', $($frame_show_id).attr('src'));
                },
                'tab-close-current': function () { // 关闭本页
                    closeTab($tab_show_id, $frame_show_id);
                },
                'tab-close-other': function () { // 关闭其他
                    var $tab_home = "#tab-home",
                        $frame_home = "#frame-home";
                    if ($tab_show_id === $tab_home) { // 当前页显示为home的时候即为关闭所有
                        closeAllTab();
                        return;
                    } else {
                        let nextAllTabs = $($tab_home).nextAll(),
                            nextAllFrames = $($frame_home).nextAll();
                        if (nextAllTabs.length < 1 || nextAllFrames.length < 1) return;
                        var _curr_id, _tab_id, _frame_id, $tab_id, $frame_id;
                        $.each(nextAllTabs, function (i, n) {
                            _curr_id = $(this).attr('tab-id');
                            _tab_id = 'tab-' + _curr_id;
                            _frame_id = 'frame-' + _curr_id;
                            $tab_id = "#" + _tab_id;
                            $frame_id = "#" + _frame_id;
                            if ($tab_show_id === $tab_id) return true;
                            closeTab($tab_id, $frame_id);
                        });
                    }
                },
                'tab-close-all': function () { // 关闭全部
                    closeAllTab();
                }
            });

            function closeAllTab() {
                let $tab_home = "#tab-home",
                    $frame_home = "#frame-home";
                let nextAllTabs = $($tab_home).nextAll(),
                    nextAllFrames = $($frame_home).nextAll();
                if (nextAllTabs.length < 1 || nextAllFrames.length < 1) return;
                $.each(nextAllTabs, function (i, n) {
                    $(this).remove();
                });
                $.each(nextAllFrames, function (i, n) {
                    $(this).remove();
                });
                if ($tab_show_id === $tab_home) return;
                showTab($tab_home, $frame_home); // 关闭后显示首页
            }
        })
    }

    // 标签页操作按钮
    function navTabHandle(containerId) {
        var _this = $(containerId);
        _this.hover(
            function () {
                _this.find('ul').removeClass('layui-hide');
                changeClass("#tab_handle_icon", "layui-icon-down", "layui-icon-up");
            }, function () {
                _this.find('ul').addClass('layui-hide');
                changeClass("#tab_handle_icon", "layui-icon-up", "layui-icon-down");
            }
        );
    }

    //首页标签点击
    function operateHomeTab() {
        $("#tab-home").click(function () {
            if ("#tab-home" === $tab_show_id) return;
            showTab('tab-home', 'frame-home');
        });
    }

    // 页签打开 | 页签切换
    function openTab() {
        $(document).on('click', '.nav-left-c', function (event) {
            let _this = $(this);
            let _id = _this.attr('id'), _href = _this.attr('lay-href'), _title = _this.text();
            let _tab_id = "tab-" + _id, _frame_id = "frame-" + _id;
            let $tab_id = "#" + _tab_id, $frame_id = "#" + _frame_id;
            // 添加页签
            var _tab = $('<li class="nav-tab-item tab-item-select" id="tab-' + _id + '" tab-id="' + _id + '">' + _title + '</li>');
            var _close = $('<i class="bk-icon tab-close-btn">ဆ</i>');
            _tab.append(_close);
            var _isOpen = $("#" + _tab_id);
            if (_isOpen && _isOpen.length > 0) { // 重复打开
                if ($tab_show_id === $tab_id) return;
                showTab(_tab_id, _frame_id);
                return;
            }
            $('.nav-tab-c').append(_tab);
            $($tab_show_id).removeClass('tab-item-select');
            // 添加iframe
            var _main_frame = $('<iframe class="content-iframe" src="' + _href + '" width="100%" height="100%" ' +
                'frameborder="0" scrolling="auto" id="' + _frame_id + '"></iframe>');
            $($frame_show_id).addClass('layui-hide');
            $('#container-content').append(_main_frame);
            $tab_show_id = $tab_id;
            $frame_show_id = $frame_id;
            // tab点击事件
            _tab.click(function (event) {
                event.stopPropagation(); // 阻止事件冒泡
                var _thisTab = $(this);
                var _tabId = _thisTab.attr('id');
                var _frameId = "frame-" + _thisTab.attr('tab-id');
                if (("#" + _tabId) === $tab_show_id) return;
                showTab(_tabId, _frameId);
            });
            // close点击事件，页签关闭操作
            _close.click(function (event) {
                event.stopPropagation(); // 阻止事件冒泡
                var _this = $(this);
                var _thisTab = _this.parent();
                var _tabId = _thisTab.attr('id'), _frameId = "frame-" + _thisTab.attr('tab-id');
                var $tabId = "#" + _tabId, $frameId = "#" + _frameId;
                closeTab($tabId, $frameId);
            });
        });
    }

    // 页签关闭
    function closeTab($tabId, $frameId) {
        if ('#tab-home' === $tab_show_id) return; // 关闭忽略主页
        var _thisTab = $($tabId);
        if ($tabId === $tab_show_id) { // 关闭当前显示的
            var _showId, _showTabId, _showFrameId;
            // 如果当前页是最后一个，则显示前一个，否则显示后一个
            var _next = _thisTab.next();
            if (_next && _next.length > 0) {
                _showId = _next.attr('tab-id');
                _showTabId = _next.attr('id');
            } else {
                var _prev = _thisTab.prev();
                _showId = _prev.attr('tab-id');
                _showTabId = _prev.attr('id');
            }
            _showFrameId = "frame-" + _showId;
            showTab(_showTabId, _showFrameId);
        } else {
        }
        $($tabId).remove();
        $($frameId).remove();
    }

    // 页签显示
    function showTab(tabId, frameId) {
        /* startsWith 兼容性差
        if (!tabId.startsWith("#")) {
            tabId = "#" + tabId;
        }
        if (!frameId.startsWith("#")) {
            frameId = "#" + frameId;
        }*/
        if (!("#" == tabId.substr(0, 1))) {
            tabId = "#" + tabId;
        }
        if (!("#" == frameId.substr(0, 1))) {
            frameId = "#" + frameId;
        }
        let _tabSelectClass = 'tab-item-select', _frameSelectClass = 'layui-hide';
        $($tab_show_id).removeClass(_tabSelectClass);
        $(tabId).addClass(_tabSelectClass);
        $tab_show_id = tabId;
        $(frameId).removeClass(_frameSelectClass);
        $($frame_show_id).addClass(_frameSelectClass);
        $frame_show_id = frameId;
    }

    // 加载导航
    function loadMenus() {
        $.get("/sys/getMenus", function (data) {
            initMenus(data);
        }, 'json');
    }

    // 初始化导航菜单
    function initMenus(data) {
        if (data === null || !data) return;
        var _showFirst = 0; //默认显示第一个
        $.each(data, function (i, n) {
            var _menus = n.subMenus;
            if (!_menus || _menus.length < 1) return; // 沒有子菜单不显示
            // 初始化水平导航
            var _navLeftId = "nav_left_" + n.id;
            var _tli = $('<li class="layui-nav-item" navleftid="' + _navLeftId + '"><a id="' + n.id + '" href="javascript:;">' + n.menuName + '</a></li>');
            _showFirst = data.length - 1;
            if (i === _showFirst) { // 默认展示第一个
                _tli.addClass('layui-this');
                _isShow = true;
            }
            // $("#nav_top").append(_tli);
            $("#nav_top").prepend(_tli);
            // top menu 点击事件
            _tli.click(function () {
                let _nli = "#" + _navLeftId;
                $("#nav_left_p ul").addClass('layui-hide');
                $(_nli).removeClass('layui-hide');
            });
            // 初始化左边导航
            var _ul;
            if (i == _showFirst) { // 显示第一个
                _ul = $('<ul class="layui-nav layui-nav-tree" lay-shrink="all" lay-filter="nav-menus" id="' + _navLeftId + '"></ul>');
            } else {
                _ul = $('<ul class="layui-nav layui-nav-tree layui-hide" lay-shrink="all" lay-filter="nav-menus" id="' + _navLeftId + '"></ul>');
            }
            $.each(_menus, function (il, nl) {
                var _li = $('<li class="layui-nav-item layui-nav-itemed"><a id="' + nl.id + '" href="javascript:;">' + nl.menuName + '</a>');
                var _subs = nl.subMenus;
                $.each(_subs, function (ils, nls) {
                    var _dl = $('<dl class="layui-nav-child"></dl>');
                    var _dd = $('<dd><a id="' + nls.id + '" href="javascript:;" class="nav-left-c" lay-href="' + nls.url + '">' + nls.menuName + '</a></dd>');
                    $(_dl).append(_dd);
                    $(_li).append(_dl);
                });
                $(_ul).append(_li);
            });
            $("#nav_left_p").append(_ul);
        });
        // 动态加载的, 重新渲染样式
        element.render('nav', 'navTopLevel');
        element.render('nav', 'nav-menus');
    }

    /* 页面元素样式替换,用newClass替换oldClass;
     * 参数：selector -页面元素id或者class
     */
    function changeClass(selector, oldClass, newClass) {
        $(selector).removeClass(oldClass);
        $(selector).addClass(newClass);
    }

});

// 定义在 layui|jquery ready之外的函数, 如果在页面中需要调用则必须定义在ready函数之外, 否则会出现 ‘函数未定义的错误’
// iframe 自适应高度设置
function changeFrameHeight(iframe) {
    var _frame_id = iframe.id;
    var _iframe = document.getElementById(_frame_id);
    try {
        var bHeight = _iframe.contentWindow.document.body.scrollHeight;
        var dHeight = _iframe.contentWindow.document.documentElement.scrollHeight;
        var height = Math.max(bHeight, dHeight);
        _iframe.height = height;
        $("#container-content").attr("height", height);
    } catch (ex) {
    }
}

/**
 * iframe自适应高度，height为手动设置的最小高度
 */
/*function changeFrameHeight(_iframe) {
    // var height = 150;
    var _iframe_id = _iframe.id;

    var iframe = parent.document.getElementById(_iframe_id);
    let subDoc = iframe.contentWindow.document;
    var subDocBody = subDoc.body;
    var bHeight = subDocBody.scrollHeight
        , dHeight = subDoc.documentElement.scrollHeight;
    var height = Math.max(bHeight, dHeight);
    var realHeight;
    var userAgent = navigator.userAgent;
    //谷歌浏览器特殊处理
    if (userAgent.indexOf("Chrome") > -1) {
        realHeight = subDoc.documentElement.scrollHeight;
    } else {
        realHeight = subDocBody.scrollHeight;
    }
    if (realHeight < height) {
        $(iframe).height(height);
    } else {
        $(iframe).height(realHeight);
    }
}*/




$(function () {
    layui.extend({
        eleTree: '/plugins/layui-extend/eleTree'
    }).use(['eleTree'], function () {
        var eleTree = layui.eleTree;
        var eleT = eleTree.render({
            elem: '.ele',
            defaultExpandAll: true,
            showCheckbox: true,
            // showLine: true,
            data: [
                {
                    "id": 1,
                    "label": "安徽省",
                    "children": [
                        {
                            "id": 2,
                            "label": "马鞍山市",
                            "disabled": true,
                            "children": [
                                {
                                    "id": 3,
                                    "label": "和县"
                                },
                                {
                                    "id": 4,
                                    "label": "花山区",
                                    "checked": true
                                }
                            ]
                        },
                        {
                            "id": 22,
                            "label": "淮北市",
                            "children": [
                                {
                                    "id": 23,
                                    "label": "濉溪县"
                                },
                                {
                                    "id": 24,
                                    "label": "相山区",
                                    "checked": true
                                }
                            ]
                        }
                    ]
                },
                {
                    "id": 5,
                    "label": "河南省",
                    "children": [
                        {
                            "id": 6,
                            "label": "郑州市"
                        }
                    ]
                },
                {
                    "id": 10,
                    "label": "江苏省",
                    "children": [
                        {
                            "id": 11,
                            "label": "苏州市"
                        },
                        {
                            "id": 12,
                            "label": "南京市",
                            "children": [
                                {
                                    "id": 13,
                                    "label": "姑苏区"
                                },
                                {
                                    "id": 14,
                                    "label": "相城区"
                                }
                            ]
                        }
                    ]
                }
            ],

        });
        // 获取选中的值
        $('.getChecked').click(function () {
            var c = eleT.getChecked(false, true);
        });
        // 全选 /反选
        $('.checkedAll').click(function () {
            let $ele = $(".ele input:checkbox");
            let checkedAllFlag = $("#checkedAllFlag").val();
            if (0 == checkedAllFlag) {
                $ele.attr('checked', true);
                $ele.attr('eletree-status', '1');
                $(".eleTree-checkbox").addClass('eleTree-checkbox-checked');
                $(".layui-icon").addClass('layui-icon-ok');
                $("#checkedAllFlag").val("1");
            } else {
                $ele.attr('checked', false);
                $ele.attr('eletree-status', '0');
                $(".eleTree-checkbox").removeClass('eleTree-checkbox-checked');
                $(".layui-icon").removeClass('layui-icon-ok');
                $("#checkedAllFlag").val("0");
            }
        });
    });
});
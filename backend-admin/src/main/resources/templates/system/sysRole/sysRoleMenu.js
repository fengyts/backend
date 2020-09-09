$(function () {
    layui.extend({
        eleTree: '/plugins/layui-extend/eleTree'
    }).use(['eleTree'], function () {
        var eleTree = layui.eleTree;
        var data = [
            {
                "label": "安徽省",
                "id": "001",
                "isOpen": true,
                "children": [
                    {
                        "label": "淮北市",
                        "id": "001001"
                    },
                    {
                        "label": "马鞍山市",
                        "id": "001002",
                        "isOpen": true,
                        "children": [
                            {
                                "label": "花山区",
                                "id": "001002001",
                                "isOpen": true,
                                "children": [
                                    {
                                        "label": "霍里街道",
                                        "disabled": true,
                                        "checked": true,
                                        "id": "001002002001",
                                        "radioDisabled": true
                                    },
                                    {
                                        "label": "桃源路",
                                        "id": "001002002002"
                                    },
                                    {
                                        "label": "湖东路",
                                        "checked": true,
                                        "id": "001002002003",
                                        "radioChecked": true
                                    }
                                ]
                            },
                            {
                                "label": "雨山区",
                                "id": "001002002"
                            },
                            {
                                "label": "和县",
                                "id": "001002003"
                            }
                        ]
                    },
                    {
                        "label": "合肥市",
                        "id": "001003"
                    }
                ]
            },
            {
                "label": "河南省",
                "id": "002",
                "children": [
                    {
                        "label": "郑州市",
                        "id": "002001"
                    },
                    {
                        "label": "开封市",
                        "id": "002002"
                    }
                ]
            },
            {
                "label": "江苏省",
                "id": "003",
                "children": [
                    {
                        "label": "苏州市",
                        "id": "003001"
                    },
                    {
                        "label": "南京市",
                        "id": "003002"
                    },
                    {
                        "label": "无锡市",
                        "id": "003003"
                    },
                    {
                        "label": "徐州市",
                        "id": "003004"
                    }
                ]
            }
        ]

        var eleT = eleTree({
            el: '.eleTree',
            data: data,
            showCheckbox: true,
            highlightCurrent: true,
        })
        eleT.on('checkbox', function (data) {
            // console.log(this)
            console.log(data)
        })
        // 获取选中的值
        $('.getChecked').click(function () {
            var c = eleT.getChecked(false, true);
            console.log(c);
        });
        // 全选/反选
        $(".checkedAll").click(function () {
            eleT.setAllChecked();
            let _ca = $("#checkedAllFlag").val();
            if ('0' === _ca) {
                eleT.setAllChecked();
                $("#checkedAllFlag").val('1');
            } else {
                eleT.reverseChecked();
                $("#checkedAllFlag").val('0');
            }
        });
    });
});
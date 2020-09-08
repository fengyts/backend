$(function () {

    layui.extend({
        eleTree: '/plugins/layui-extend/eleTree'
    }).use(['eleTree'], function () {
        var eleTree = layui.eleTree;
        eleTree.render({
            elem: '.ele',
            defaultExpandAll: true,
            showCheckbox: true,
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
    });
});
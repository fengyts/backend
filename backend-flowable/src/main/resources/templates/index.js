layui.use(['form', 'table', 'layer'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer;

    $("#deployProcess").click(function(){
        $.post("/holiday/deployProcess", function (res){
            console.log(res);
        }, 'text')
    });

    $("#startProcess").click(function (){
        $.post("/holiday/startProcess", {"processDefinitionKey":"holidayRequest"}, function (res) {

        }, 'json');
    });
    
    $("#viewTask").click(function () {
        $.get("/holiday/queryTask",function (res){
            console.log(res);
        }, 'json');
    });

});
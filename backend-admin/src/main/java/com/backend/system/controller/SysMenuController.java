package com.backend.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.backend.common.ResultData;
import com.backend.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/system/sysMenu/")
public class SysMenuController {

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping("list")
    public String listPage(){
        return "system/sysMenu/list";
    }

    @GetMapping("getMenuList")
    @ResponseBody
    public ResultData getMenuList(){
        String str = "{\"data\":[{\"id\":1,\"name\":\"\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":2,\"name\":\"\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":3,\"name\":\"张三3\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":4,\"name\":\"张三4\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":5,\"name\":\"张三5\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":6,\"name\":\"张三6\",\"danxuan\":{\"name\":1,\"value\":\"张三1\"},\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":7,\"name\":\"张三7\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":8,\"name\":\"张三8\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":9,\"name\":\"张三9\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},{\"id\":10,\"name\":\"张三10\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\":\"张三1\"},{\"name\":2,\"value\":\"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"}],\"code\":0,\"msg\":\"\",\"count\":10}";
        String arrs = "[\n" +
                "    {\"id\":1,\"name\":\"\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":2,\"name\":\"\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":3,\"name\":\"张三3\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":4,\"name\":\"张三4\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":5,\"name\":\"张三5\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":6,\"name\":\"张三6\",\"danxuan\":{\"name\":1,\"value\": \"张三1\"},\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":7,\"name\":\"张三7\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":8,\"name\":\"张三8\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":9,\"name\":\"张三9\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"},\n" +
                "    {\"id\":10,\"name\":\"张三10\",\"danxuan\":null,\"duoxuan\":[{\"name\":1,\"value\": \"张三1\"},{\"name\":2,\"value\": \"张三2\"}],\"birthday\":\"\",\"aihao\":\"旅游\"}\n" +
                "  ]";
        JSONObject obj = JSONObject.parseObject(str);
        JSONArray jsonArray = JSONArray.parseArray(arrs);
        ResultData resultData = ResultData.ok(jsonArray);
        return resultData;
    }

}

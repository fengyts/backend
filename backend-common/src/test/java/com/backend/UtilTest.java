package com.backend;

import com.alibaba.fastjson.JSONObject;
import com.backend.utils.BeanMapUtil;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

public class UtilTest {

    public static void mapUtilsTest() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", "1");
        map.put("name", "zhangsan");
        User user = new User();
//        MapUtils.mapToBeanObj(map, user);

//        User u1 = MapUtils.mapToBean(map, user);
        User u1 = BeanMapUtil.mapToBean(map, User.class);
        System.out.println(JSONObject.toJSONString(u1));
        System.out.println();
    }

    public static void main(String[] args) {
        mapUtilsTest();
    }


    @Data
    static class User implements Serializable {
        private static final long serialVersionUID = 4818750111378175536L;

        private String id;
        private String name;
        private Integer age;

    }

}

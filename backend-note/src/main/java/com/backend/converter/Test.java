package com.backend.converter;

import com.alibaba.fastjson.JSONObject;
import com.backend.converter.example.UserDO;
import com.backend.converter.example.UserDto;
import java.util.Date;
import org.mapstruct.factory.Mappers;

public class Test {

    // 实际开发中可以在service层使用 @AutoWired注解注入
    static UserConverter converter = UserConverter.CONVERTER;


    public static void toDto() {
        UserDO entity = new UserDO();
        entity.setId("1");
        entity.setAge(18);
        entity.setUserName("zhangsan");
        entity.setBirthday(new Date());

//        UserDto dto = UserConverter.CONVERTER.toDto(entity);
        UserDto dto = converter.toDto(entity);
        print(dto);

    }

    public static void toEntity(){
        UserDto dto = new UserDto();
        dto.setId("2");
        dto.setName("lisi");
        dto.setAge(20);
        dto.setBirthday("2020-01-10 11:35:33");
        dto.setEndDay("2020-01-10");

//        UserDO userDO = UserConverter.CONVERTER.toEntity(dto);
        UserDO userDO = converter.toEntity(dto);
        print(userDO);
    }

    public static void print(Object obj){
        System.out.println(JSONObject.toJSONString(obj));
    }


    public static void main(String[] args) {
        toDto();
        toEntity();
    }
}

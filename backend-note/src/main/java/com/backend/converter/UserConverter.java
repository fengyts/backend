package com.backend.converter;

import com.backend.converter.example.UserDO;
import com.backend.converter.example.UserDto;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 使用 mapstruct框架 对 dto和entity实体类之间互相转换
 * 使用该框架比BeanUtils效率更高
 * @author fengyts
 */
@Mapper
public interface UserConverter {

    UserConverter CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mapping(source = "userName", target = "name")
    @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd HH:mm:ss")
    UserDto toDto(UserDO entity);

    @Mapping(source = "name", target = "userName")
    @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "endDay", expression = "java(parse(dto.getEndDay(), \"yyyy-MM-dd\"))")
//    @Mapping(target = "endDay", expression = "java(parse(dto.getEndDay(), null))")
    UserDO toEntity(UserDto dto);

    default Date parse(String dateStr, String format) {
        try {
            if(null == dateStr || "".equals(dateStr)){
                return null;
            }
            if (null == format || "".equals(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            DateFormat sdf = new SimpleDateFormat(format);
            Date parse = sdf.parse(dateStr);
            return parse;
        } catch (ParseException e) {
        }
        return null;
    }

}

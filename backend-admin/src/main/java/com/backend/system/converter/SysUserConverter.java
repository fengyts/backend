package com.backend.system.converter;

import com.backend.common.Constants;
import com.backend.common.EntityConverter;
import com.backend.system.dto.SysUserDto;
import com.backend.system.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author
 */
@Mapper(componentModel = "spring", uses = {SysUser.class, SysUserDto.class})
public interface SysUserConverter extends EntityConverter<SysUser, SysUserDto> {

    @Mappings({
            @Mapping(source = "modifyTime", target = "modifyTime", ignore = true),
            @Mapping(source = "createTime", target = "createTime", ignore = true)
    })
    @Override
    SysUser toEntity(SysUserDto dto);

    @Mappings({
            @Mapping(target = "modifyTime", source = "modifyTime", dateFormat = Constants.DATE_FORMAT_MILLI_SECONDS),
            @Mapping(target = "createTime", source = "createTime", dateFormat = Constants.DATE_FORMAT_SECONDS)
    })
    @Override
    SysUserDto toEntityDto(SysUser entity);

}

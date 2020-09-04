package com.backend.system.converter;

import com.backend.common.Constants;
import com.backend.common.EntityConverter;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.entity.SysMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author
 */
@Mapper(componentModel = "spring")
public interface SysMenuConverter extends EntityConverter<SysMenu, SysMenuDto> {

    @Mappings({
            @Mapping(source = "modifyTime", target = "modifyTime", ignore = true),
            @Mapping(source = "createTime", target = "createTime", ignore = true),
    })
    @Override
    SysMenu toEntity(SysMenuDto dto);

    @Mappings({
            @Mapping(target = "modifyTime", source = "modifyTime", dateFormat = Constants.DATE_FORMAT_MILLI_SECONDS),
            @Mapping(target = "createTime", source = "createTime", dateFormat = Constants.DATE_FORMAT_SECONDS),
    })
    @Override
    SysMenuDto toEntityDto(SysMenu entity);

}

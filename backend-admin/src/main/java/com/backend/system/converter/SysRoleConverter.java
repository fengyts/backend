package com.backend.system.converter;

import com.backend.common.Constants;
import com.backend.common.EntityConverter;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter extends EntityConverter<SysRole, SysRoleDto> {

    @Mappings({
            @Mapping(source = "modifyTime", target = "modifyTime", ignore = true),
            @Mapping(source = "createTime", target = "createTime", ignore = true),
    })
    @Override
    SysRole toEntity(SysRoleDto dto);

    @Mappings({
            @Mapping(target = "modifyTime", source = "modifyTime", dateFormat = Constants.DATE_FORMAT_MILLI_SECONDS),
            @Mapping(target = "createTime", source = "createTime", dateFormat = Constants.DATE_FORMAT_SECONDS),
    })
    @Override
    SysRoleDto toEntityDto(SysRole entity);

}

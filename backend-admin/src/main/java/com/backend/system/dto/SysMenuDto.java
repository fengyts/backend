package com.backend.system.dto;

import com.backend.common.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class SysMenuDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer level;
    private Long parentId;
    private String menuName;
    private Integer sort;
    private String url;
    private String icon;
    private Integer isShow;

    private List<SysMenuDto> subMenus;


}

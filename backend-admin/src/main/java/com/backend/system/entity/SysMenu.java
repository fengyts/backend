package com.backend.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.backend.common.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author fengyts
 * @since 2020-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_menu")
public class SysMenu extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单类型：0-目录；1-菜单；2-按钮
     */
    private Integer type;

    /**
     * 级别：0-系统级；1-顶级top菜单；2-一级菜单；3-二级菜单；4-三级菜单
     */
    private Integer level;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 菜单url
     */
    private String url;

    /**
     * 菜单图标icon
     */
    private String icon;

    /**
     * 是否显示: 0-不显示；1-显示
     */
    private Integer isShow;

    /**
     * 是否删除：0-未删除；1-已删除
     */
    private Integer isDelete;


}

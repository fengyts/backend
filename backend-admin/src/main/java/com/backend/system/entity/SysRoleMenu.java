package com.backend.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.backend.common.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统角色-菜单表
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_role_menu")
public class SysRoleMenu extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;


}

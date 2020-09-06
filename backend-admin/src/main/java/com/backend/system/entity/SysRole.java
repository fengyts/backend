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
 * 用户角色表
 * </p>
 *
 * @author fengyts
 * @since 2020-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_role")
public class SysRole extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态(是否可用): 1-可用；0-不可用
     */
    private Integer status;


}

package com.backend.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.backend.common.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统用户-角色表
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_user_role")
public class SysUserRole extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;


}

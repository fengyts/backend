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
 * 系统用户权限表
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_permission")
public class SysPermission extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


}

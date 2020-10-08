package com.backend.model.entity;

import lombok.Data;

/**
 * @author fengyts
 */
@Data
public class SysUserEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String loginName;
    private String realName;
    private String passwd;
    private String email;
    private String mobile;
    private Integer sex;
    private Integer age;
    private String identityCard;
    /**部门*/
    private String deptId;
    /**职务 */
    private String position;
    /**分组*/
    private String groupId;

}

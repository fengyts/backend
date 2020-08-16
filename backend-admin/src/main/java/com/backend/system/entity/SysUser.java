package com.backend.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.backend.common.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author fengyts
 * @since 2020-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_sys_user")
public class SysUser extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名/登陆账号
     */
    private String userName;

    /**
     * 昵称/真实姓名
     */
    private String nickName;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别：0-女；1-男；2-未知
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户类型：0-系统用户；1-其他用户
     */
    private Integer type;

    /**
     * 用户状态：0-冻结；1-正常
     */
    private Integer status;

    /**
     * 最后登陆ip
     */
    private String latestLoginIp;

    /**
     * 最后登陆时间
     */
    private LocalDateTime latestLoginTime;


}

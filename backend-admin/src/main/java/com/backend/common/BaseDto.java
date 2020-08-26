package com.backend.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long createUserId;
    private String createTime;
    private Long modifyUserId;
    private String modifyTime;

}

package com.backend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long createUserId;

    private String createTime;

    private Long modifyUserId;

    private String modifyTime;


}

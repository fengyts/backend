package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @file: com.chengtay.clearing.api.constant.DeleteEnum
 * @desc:状态枚举类
 * @date:2019/11/22 16:34
 * @author:jian.zh
 **/
@Getter
@RequiredArgsConstructor
public enum StatusEnum {
	/**  **/
	VALID(1, "有效"),
	/**  **/
	INVALID(0, "无效");

	private final Integer code;
	private final String desc;

}

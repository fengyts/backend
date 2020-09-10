package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @file: com.chengtay.clearing.api.constant.DeleteEnum
 * @desc: 系统菜单显示状态枚举类
 * @date:2019/11/22 16:34
 * @author:jian.zh
 **/
@Getter
@RequiredArgsConstructor
public enum SysMenuShowStatusEnum {
	/**  **/
	VALID(1, "显示"),
	/**  **/
	INVALID(0, "隐藏");

	private final Integer code;
	private final String desc;

}

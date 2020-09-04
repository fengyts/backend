package com.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author DELL
 */
@Getter
@RequiredArgsConstructor
public enum SysMenuTypeEnum {

    DIRECTORY(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    private final int code;
    private final String desc;

}

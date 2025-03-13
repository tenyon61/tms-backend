package com.tms.web.model.enums.sys;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述
 *
 * @author tenyon
 * @date 2025/3/10
 */
public enum MenuTypeEnum {
    DIRECT("目录", 0),
    MENU("菜单", 1),
    BUTTON("按钮", 2);

    private final String text;

    private final int value;

    MenuTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.asList(values()).stream().map(item -> item.value).collect(Collectors.toList());
    }


    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

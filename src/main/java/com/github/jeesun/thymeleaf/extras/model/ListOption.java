package com.github.jeesun.thymeleaf.extras.model;

import java.io.Serializable;

/**
 * 简单的字典
 *
 * @author simon
 * @date 2018-11-25
 **/

public class ListOption implements Serializable {
    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ListOption{" +
                "value='" + value + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

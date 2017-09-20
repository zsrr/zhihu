package com.stephen.zhihu.base;


public class UpdateMessageInfo {
    private final String field;
    private final Object value;

    public UpdateMessageInfo(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}

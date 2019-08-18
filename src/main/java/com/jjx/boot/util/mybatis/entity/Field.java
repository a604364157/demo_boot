package com.jjx.boot.util.mybatis.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Field implements Serializable {

    private static final long serialVersionUID = 685780039167666489L;

    private String field;

    private String key;

    private String none;

    private String type;

    public String getField() {
        return field;
    }
    @JSONField(name = "Field")
    public void setField(String field) {
        this.field = field;
    }

    public String getKey() {
        return key;
    }
    @JSONField(name = "Key")
    public void setKey(String key) {
        this.key = key;
    }

    public String getNone() {
        return none;
    }
    @JSONField(name = "Null")
    public void setNone(String none) {
        this.none = none;
    }

    public String getType() {
        return type;
    }
    @JSONField(name = "Type")
    public void setType(String type) {
        this.type = type;
    }
}

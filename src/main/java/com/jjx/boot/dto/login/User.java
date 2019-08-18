package com.jjx.boot.dto.login;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class User {
    @JSONField(name = "NAME")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Max(value = 120L, message = "年龄不能大于120岁")
    @Min(value = 16L, message = "年龄不能小于16岁")
    @JSONField(name = "AGE")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

package com.jjx.boot.dto.twocolor;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author Administrator
 */
public class RandDTO {

    @JSONField(name = "TIMES")
    public String times;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "RandDTO{" +
                "times='" + times + '\'' +
                '}';
    }
}

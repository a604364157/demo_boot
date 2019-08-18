package com.jjx.boot.entity;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 5443003266202924832L;
    private long no;

    private String name;

    private double fee;

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}

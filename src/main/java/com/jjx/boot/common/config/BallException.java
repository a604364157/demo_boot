package com.jjx.boot.common.config;

/**
 * @author Administrator
 */
public class BallException extends RuntimeException {

    private static final long serialVersionUID = -5666988411412644677L;
    private String code;
    private String msg;

    public BallException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

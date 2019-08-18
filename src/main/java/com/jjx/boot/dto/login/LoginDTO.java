package com.jjx.boot.dto.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author jjx
 */
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = -7836464409986785904L;
    @JSONField(name = "LOGIN_NO")
    private String loginNo;
    @JSONField(name = "PASSWORD")
    private String password;
    @JSONField(name = "AUTH_CODE")
    private String authCode;

    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

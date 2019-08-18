package com.jjx.boot.dto.twocolor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Administrator
 */
public class UptBallInDTO {
    @JSONField(name = "RED1")
    private String red1;
    @JSONField(name = "RED2")
    private String red2;
    @JSONField(name = "RED3")
    private String red3;
    @JSONField(name = "RED4")
    private String red4;
    @JSONField(name = "RED5")
    private String red5;
    @JSONField(name = "RED6")
    private String red6;
    @JSONField(name = "BLUE")
    private String blue;
    @JSONField(name = "RUN_DATE")
    private String runDate;
    @JSONField(name = "ID_NO")
    @Valid
    private String idNo;
    private Date runDateP;
    private Timestamp opTimeP;

    public String getRed1() {
        return red1;
    }

    public void setRed1(String red1) {
        this.red1 = red1;
    }

    public String getRed2() {
        return red2;
    }

    public void setRed2(String red2) {
        this.red2 = red2;
    }

    public String getRed3() {
        return red3;
    }

    public void setRed3(String red3) {
        this.red3 = red3;
    }

    public String getRed4() {
        return red4;
    }

    public void setRed4(String red4) {
        this.red4 = red4;
    }

    public String getRed5() {
        return red5;
    }

    public void setRed5(String red5) {
        this.red5 = red5;
    }

    public String getRed6() {
        return red6;
    }

    public void setRed6(String red6) {
        this.red6 = red6;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getRunDateP() {
        return runDateP;
    }

    public void setRunDateP(Date runDateP) {
        this.runDateP = runDateP;
    }

    public Timestamp getOpTimeP() {
        return opTimeP;
    }

    public void setOpTimeP(Timestamp opTimeP) {
        this.opTimeP = opTimeP;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

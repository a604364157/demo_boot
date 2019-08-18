package com.jjx.boot.dto.twocolor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Date;

/**
 * @author Administrator
 */
public class QryBallInDTO {
    @JSONField(name = "ID_NO")
    private String idNo;
    @JSONField(name = "RUN_DATE")
    private String runDate;
    @JSONField(name = "PAGE_SIZE")
    private int pageSize;
    @JSONField(name = "PAGE_NUM")
    private int pageNum;

    private Date time;

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

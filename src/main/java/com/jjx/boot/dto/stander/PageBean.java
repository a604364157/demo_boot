package com.jjx.boot.dto.stander;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 */
@SuppressWarnings("unused")
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = -5730656761340546934L;

    @JSONField(name = "PAGE_NUM")
    private int pageNum;
    @JSONField(name = "PAGE_SUM")
    private long pageSum;
    @JSONField(name = "PAGE_SIZE")
    private int pageSize;
    @JSONField(name = "DATAS")
    private List<T> datas;

    public PageBean() {
    }

    public PageBean(long pageSum, List<T> datas) {
        this.pageSum = pageSum;
        this.datas = datas;
    }

    public PageBean(int pageNum, int pageSum, int pageSize, List<T> datas) {
        this.pageNum = pageNum;
        this.pageSum = pageSum;
        this.pageSize = pageSize;
        this.datas = datas;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSum() {
        return pageSum;
    }

    public void setPageSum(int pageSum) {
        this.pageSum = pageSum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}

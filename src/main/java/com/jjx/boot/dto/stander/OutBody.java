package com.jjx.boot.dto.stander;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class OutBody<T> implements Serializable {
    private static final long serialVersionUID = -4552746027977238142L;

    /**
     * 执行成功标志 0成功1失败
     */
    @JSONField(name = "STATUS")
    private String status;
    /**
     * 服务返回信息，通常为错误信息
     */
    @JSONField(name = "MSG")
    private String msg;
    /**
     * 服务返回数据
     */
    @JSONField(name = "OUT_DATA")
    private T outData;

    public OutBody() {
    }

    public OutBody(T outData) {
        this.outData = outData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getOutData() {
        return outData;
    }

    public void setOutData(T outData) {
        this.outData = outData;
    }

}

package com.jjx.boot.dto.stander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class InDTO<T> implements Serializable {

    private static final long serialVersionUID = -1189223960511421729L;
    @JSONField(name = "ROOT")
    private HeaderBody<T> headerBody;

    private HeaderBody<T> getHeaderBody() {
        return this.headerBody;
    }

    public void setHeaderBody(HeaderBody<T> headerBody) {
        this.headerBody = headerBody;
    }

    public T getBody() {
        return this.headerBody.getBody();
    }

    public void setBody(T t) {
        HeaderBody<T> bodyHeader = new HeaderBody<>();
        bodyHeader.setBody(t);
        this.setHeaderBody(headerBody);
    }

    public void setHeader(JSONObject header) {
        JSONObject newHeader = header;
        if (this.getHeader() == null) {
            newHeader = new JSONObject();
            if (header != null) {
                newHeader.putAll(header);
            }
        }
        this.getHeaderBody().setHeader(newHeader);
    }

    public JSONObject getHeader() {
        return this.headerBody.getHeader();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

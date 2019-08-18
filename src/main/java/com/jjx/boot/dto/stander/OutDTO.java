package com.jjx.boot.dto.stander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class OutDTO<T> implements Serializable {

    private static final long serialVersionUID = 4602714741495982191L;
    @JSONField(name = "ROOT")
    public HeaderBody<OutBody<T>> headerBody;

    public OutDTO() {
        this.headerBody = this.createHeaderBody();
    }

    public OutDTO(OutBody<T> body) {
        this.headerBody = this.createHeaderBody();
        this.setBody(body);
    }

    public void setBodyOutData(T t) {
        this.setBody(new OutBody<>(t));
    }

    public void setBody(OutBody<T> body) {
        this.headerBody = this.createHeaderBody();
        this.headerBody.setBody(body);
    }

    private HeaderBody<OutBody<T>> createHeaderBody() {
        return this.headerBody != null ? this.headerBody : new HeaderBody<>();
    }

    public HeaderBody<OutBody<T>> getHeaderBody() {
        return headerBody;
    }

    public void setHeaderBody(HeaderBody<OutBody<T>> headerBody) {
        this.headerBody = headerBody;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

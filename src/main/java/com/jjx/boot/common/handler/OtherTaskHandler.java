package com.jjx.boot.common.handler;

import java.util.Map;

/**
 * @author jiangjx
 */
public class OtherTaskHandler extends AbstractHandler {

    @Override
    public int index() {
        return 1;
    }

    @Override
    protected void doBefore(Map<String, Object> param) throws Throwable {
        System.out.println(getClass().getName()+"执行了");
    }

}

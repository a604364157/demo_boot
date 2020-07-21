package com.jjx.boot.common.handler;

import java.util.Map;

/**
 * @author jiangjx
 */
public class DefaultTaskHandler extends AbstractHandler {

    @Override
    public int index() {
        return 0;
    }

    @Override
    protected void doBefore(Map<String, Object> param) throws Throwable {
        System.out.println(getClass().getName()+"执行了");
    }

}

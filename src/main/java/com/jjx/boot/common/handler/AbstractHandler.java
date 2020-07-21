package com.jjx.boot.common.handler;

import java.util.Map;

/**
 * @author jiangjx
 */
public abstract class AbstractHandler {

    AbstractHandler nextDbHandler = null;

    public final void before(Map<String, Object> param) throws Throwable {
        doBefore(param);
        if (nextDbHandler != null) {
            nextDbHandler.before(param);
        }
    }

    /**
     * 执行顺序
     *
     * @return 执行顺序
     */
    public abstract int index();

    /**
     * 前置处理函数
     *
     * @param param 动态入参
     * @throws Throwable 异常
     */
    protected abstract void doBefore(Map<String, Object> param) throws Throwable;
}

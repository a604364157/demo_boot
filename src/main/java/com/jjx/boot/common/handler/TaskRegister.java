package com.jjx.boot.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * @author jiangjx
 */

public enum TaskRegister {

    /**
     * 枚举单列实例
     */
    INSTANCE;
    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 默认的处理器
     */
    private AbstractHandler handler = new DefaultTaskHandler();

    /**
     * 获取操作处理器
     *
     * @return handler
     */
    public AbstractHandler getHandler() {
        return handler;
    }

    public void init(List<AbstractHandler> handlers) {
        this.handler = build(handlers);
        log.info("完成注册操作处理器:[{}]", handlers);
    }

    private AbstractHandler build(List<AbstractHandler> handlers) {
        if (handlers.size() > 1) {
            // 对操作拦截器先进行排序
            handlers.sort(Comparator.comparingInt(AbstractHandler::index));
            //构造链表
            for (int i = 0, j = handlers.size(); i < j; i++) {
                if (i < j - 1) {
                    handlers.get(i).nextDbHandler = handlers.get(i + 1);
                }
            }
        }
        return handlers.get(0);
    }
}

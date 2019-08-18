package com.jjx.boot.pool;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class PoolConfig implements Serializable {

    private static final long serialVersionUID = -8558526504004102090L;

    private int minPoolSize;
    private int maxPoolSize;
    private int keepTime;
    private int maxQueueSize;

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(int keepTime) {
        this.keepTime = keepTime;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
}

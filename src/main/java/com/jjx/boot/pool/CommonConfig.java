package com.jjx.boot.pool;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class CommonConfig implements Serializable {

    private static final long serialVersionUID = -4003122870602378964L;
    private PoolConfig pool;

    public PoolConfig getPool() {
        return pool;
    }

    public void setPool(PoolConfig poolConfig) {
        this.pool = poolConfig;
    }
}

package com.jjx.boot.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Configuration
@EnableCaching
public class CaffeineCacheCfg {

    private static final int DEFAULT_MAXSIZE = 50000;
    private static final int DEFAULT_TTL = 10;

    /**
     * 定義cache名稱、超時時長（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在                构造方法的参数中指定。
     */
    public enum Caches{
        /**
         * 有效期5秒
         */
        getPersonById(5),
        /**
         * 缺省10秒
         */
        getSomething,
        /**
         * 5分钟，最大容量1000
         */
        getOtherthing(300, 1000)
        ;

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        private int maxSize=DEFAULT_MAXSIZE;
        private int ttl=DEFAULT_TTL;

        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        for(Caches c : Caches.values()){
            caches.add(new CaffeineCache(c.name(), Caffeine.newBuilder().recordStats().expireAfterWrite(c.getTtl(), TimeUnit.SECONDS).maximumSize(c.getMaxSize()).build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}

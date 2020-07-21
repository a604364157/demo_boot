package com.jjx.boot.common.util;

import com.alibaba.fastjson.JSON;
import com.jjx.boot.common.pool.PoolConfig;
import com.jjx.boot.common.pool.ThreadPoolSingle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Administrator
 */
public class YamlUtil {

    public static String getYaml(String fileName, String attrName) {
        Yaml yaml = new Yaml();
        InputStream in = YamlUtil.class.getResourceAsStream("/" + fileName);
        @SuppressWarnings("unchecked")
        Map<String, Object> tmp = yaml.loadAs(in, Map.class);
        return StringTool.getValueByPath(tmp, attrName);
    }

    public static <T> T getYaml(String fileName, Class<T> clazz) {
        Yaml yaml = new Yaml();
        InputStream in = YamlUtil.class.getResourceAsStream("/" + fileName);
        yaml.load(in);
        return yaml.loadAs(in, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getYaml(String fileName, String attr, Class<T> clazz) {
        Yaml yaml = new Yaml();
        InputStream in = YamlUtil.class.getResourceAsStream("/" + fileName);
        Map<String, Object> tmp = yaml.loadAs(in, Map.class);
        tmp = (Map<String, Object>) tmp.get(attr);
        return com.jjx.boot.common.util.MapBean.mapToObject(tmp, clazz, false);
    }

    public static void main(String[] args) {
        ThreadPoolTaskScheduler scheduler = ThreadPoolSingle.getSingle().getScheduler();
        scheduler.schedule(() -> {
            PoolConfig config = YamlUtil.getYaml("common.yml", "pool", PoolConfig.class);
            System.out.println(JSON.toJSONString(config));
        }, new CronTrigger("0/10 * *  * * ? "));
    }
}

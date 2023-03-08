package cn.shh.project.reggie.common.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存
 */
@Component
public class LocalCache {
    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>(64);

    public void set(String key, Object val){
        CACHE.put(key, val);
    }

    public Object get(String key){
        return CACHE.get(key);
    }
}

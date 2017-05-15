package com.siebre.basic.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRedisCacheService extends ICacheService {
    String getStringValue(String key);

    void delete(Set<String> keys);

    Set<String> keys(String keyPattern);

    void delete(String key);

    Long putListMap(String key, List<Map<String, Object>> list);

    List<Map<String, Object>> getListMap(String key, long start, long end);

    Long addToSet(String key, Object... values);

    Set getSetMembers(String key);
}

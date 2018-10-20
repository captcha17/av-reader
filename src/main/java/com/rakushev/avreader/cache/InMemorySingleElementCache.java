package com.rakushev.avreader.cache;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySingleElementCache implements Cache {
    private static final int FIRST_ELEMENT_KEY = 1;
    private Map<Integer, String> cache = new ConcurrentHashMap<>(1);

    @Override
    public boolean isMostRecent(String value) {
        String storedValue = cache.get(FIRST_ELEMENT_KEY);
        return !StringUtils.isEmpty(value) && storedValue.equalsIgnoreCase(value);
    }

    @Override
    public void updateMostRecent(String value) {
        cache.put(FIRST_ELEMENT_KEY, value);
    }
}

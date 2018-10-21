package com.rakushev.avreader.cache;

import java.util.HashSet;
import java.util.Set;

public class InMemoryCache<String> implements Cache<String> {
    private Set<String> cache = new HashSet<>();

    @Override
    public void add(String element) {
        cache.add(element);
    }

    @Override
    public void addAll(Set<String> elements) {
        cache.addAll(elements);
    }

    @Override
    public boolean contains(String element) {
        return cache.contains(element);
    }

    @Override
    public void remove(String element) {
        cache.remove(element);
    }
}

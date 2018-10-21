package com.rakushev.avreader.cache;

import java.util.Set;

public interface Cache<T> {
    void add(T t);
    void addAll(Set<T> set);
    void remove(T t);
    boolean contains(T t);
}

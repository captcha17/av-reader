package com.rakushev.avreader.cache;

public interface Cache {
    boolean isMostRecent(String value);
    void updateMostRecent(String value);
}

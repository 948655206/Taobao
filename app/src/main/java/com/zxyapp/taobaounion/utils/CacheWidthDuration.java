package com.zxyapp.taobaounion.utils;

public class CacheWidthDuration {


    private long duration;
    private String cache;

    public CacheWidthDuration(long duration, String cache) {
        this.duration = duration;
        this.cache = cache;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}

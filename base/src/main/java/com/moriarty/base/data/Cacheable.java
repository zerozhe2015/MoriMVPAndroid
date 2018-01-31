package com.moriarty.base.data;

public class Cacheable {

    private transient boolean fromCache;

    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }
}

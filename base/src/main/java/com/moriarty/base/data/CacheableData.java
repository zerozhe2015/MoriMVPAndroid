package com.moriarty.base.data;

public class CacheableData<T> {

    T data;

    boolean fromCache;


    public CacheableData(T data, boolean fromCache) {
        this.data = data;
        this.fromCache = fromCache;
    }

    public CacheableData(T data) {
        this(data, false);
    }

    public T getData() {
        return data;
    }

    public boolean isFromCache() {
        return fromCache;
    }
}

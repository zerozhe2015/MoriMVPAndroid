package com.moriarty.base.eventbus;

/**
 * 自定义eventBus接受事件执行回掉
 *
 * @param <T>
 */
public interface IListener<T> {
    /**
     *
     * @param event
     */
    void onReceiveEvent(T event);
}
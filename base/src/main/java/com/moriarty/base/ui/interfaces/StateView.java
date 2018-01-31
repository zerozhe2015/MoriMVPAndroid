package com.moriarty.base.ui.interfaces;

public interface StateView {

    /**
     * 是否view被创建
     *
     * @return
     */
    boolean isViewCreated();


    /**
     * 是否view被放置在布局中
     *
     * @return
     */
    boolean isViewLayoutCompleted();

    /**
     * 是否view被放置在布局中
     *
     * @return
     */
    boolean isViewDestroyed();


    /**
     * 在view创建之后执行的一个回调
     *
     * @param r
     */
    void setOnPostViewCreateCallback(Runnable r);


    /**
     * 在view 第一次布局的时候执行的回调
     *
     * @param r
     */

    void setOnViewFirstLayoutCallback(Runnable r);


    /**
     * 在每次start时执行的回调
     *
     * @param r
     */
    void setOnStartCallback(Runnable r);
}

package com.moriarty.morimvpandroid.configuration;


/**
 * 环境分离接口，一些跟环境相关的配置参数可以在此接口中添加相应方法
 */
public interface AppConfiguration {
    /**
     * 获取服务器的baseUrl
     * 要以/作为结尾
     *
     * @return
     */
    String getServerBaseUrl();


    String getH5PageBaseUrl();


//    // 是否显示 浮动盈亏相关
//    boolean isShowProfit();
}

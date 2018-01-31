package com.moriarty.morimvpandroid.configuration;


/**
 * 开发环境配置参数
 */
public class DebugConfig implements AppConfiguration {


    public DebugConfig() {

    }

    @Override
    public String getServerBaseUrl() {
        return "https:/www.baidu.com/";
    }

    @Override
    public String getH5PageBaseUrl() {
        return "https:/www.baidu.com/";
    }

}
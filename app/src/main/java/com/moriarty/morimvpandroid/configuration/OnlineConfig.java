package com.moriarty.morimvpandroid.configuration;


import com.moriarty.base.BaseApplication;
import com.moriarty.base.sc.SecurityCheck;

/**
 * 生产环境配置参数
 */

public class OnlineConfig implements AppConfiguration {


    public OnlineConfig() {
        //检测
        if (!SecurityCheck.check()) {
            BaseApplication.exit();
        }
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
package com.moriarty.base.http;

import android.support.v4.util.ArrayMap;

import com.moriarty.base.BaseApplication;
import com.moriarty.base.util.DeviceIdUtls;
import com.moriarty.base.util.PackageUtils;

import java.util.Map;


public class BasicCommonRequestParamsProvider implements CommonRequestParamsProvider {


    private static Map<String, String> staticCommonParams;
    public static String OSTYPE_ANDROID = "1";

    static {
        staticCommonParams = new ArrayMap<>();
        staticCommonParams.put("appVersion", PackageUtils.getCurrentVersionName());
//        staticCommonParams.put("appVersion", "1.0.0");
        staticCommonParams.put("osType", OSTYPE_ANDROID);
        staticCommonParams.put("deviceId", DeviceIdUtls.getUniversalID(BaseApplication.getApplication()));
        staticCommonParams.put("osVersion", android.os.Build.VERSION.RELEASE);
        staticCommonParams.put("channelPath", "2000"/*PackageUtils.getChannel()*/);
        staticCommonParams.put("methodVersion", "1.0");
    }

    @Override
    public Map<String, String> provideCommonRequestParams() {
        Map<String, String> commonParams = new ArrayMap<>();
        commonParams.putAll(staticCommonParams);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        commonParams.put("timeStamp", timeStamp);
        return commonParams;
    }
}

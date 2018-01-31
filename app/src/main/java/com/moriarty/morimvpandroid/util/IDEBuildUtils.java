package com.moriarty.morimvpandroid.util;


import com.moriarty.morimvpandroid.BuildConfig;
import com.moriarty.morimvpandroid.configuration.AppConfiguration;
import com.moriarty.morimvpandroid.configuration.DebugConfig;
import com.moriarty.morimvpandroid.configuration.OnlineConfig;
import com.moriarty.morimvpandroid.configuration.TestConfig;

/**
 * 一些使用android studio相关，或者跟构建环境相关的一些方法
 */
public class IDEBuildUtils {

    /**
     * ide 是否使用 android studio,一切使用到android studio 特性的方法 都要先判断这个
     *
     * @return
     */

    public static boolean isIdeAndroidStudio() {
        return true;
    }


    /**
     * 是否根据buildType 自动获取config
     */
    public static boolean isAutoGetConfigByBuildType() {
        return isIdeAndroidStudio();
    }


    /**
     * debug  对应 DebugConfig
     * <p/>
     * mstest对应 TestConfig
     * <p/>
     * realse对应 OnlineConfig
     *
     * @return
     */

    public static AppConfiguration getConfigByBuildType() {
        if (isBuildTypeDebug()) {
            return new DebugConfig();
        } else if (isBuildTypeTest()) {
            return new TestConfig();
        } else if (isBuildTypeRelease()) {
            return new OnlineConfig();
        }
        return new OnlineConfig();
    }


    /**
     * buildType 为debug
     *
     * @return
     */

    public static boolean isBuildTypeDebug() {
        return "debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE);
    }

    /**
     * buildType ebtest
     *
     * @return
     */

    public static boolean isBuildTypeTest() {
        return "ebtest".equalsIgnoreCase(BuildConfig.BUILD_TYPE);
    }

    /**
     * buildType 为release
     *
     * @return
     */
    public static boolean isBuildTypeRelease() {
        return "release".equalsIgnoreCase(BuildConfig.BUILD_TYPE);
    }

}

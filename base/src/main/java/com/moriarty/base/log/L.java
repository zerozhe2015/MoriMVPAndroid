package com.moriarty.base.log;

import android.content.Intent;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.orhanobut.logger.Logger;

public class L {


    public static boolean logEnable = true;// log总开关

    private static final String TAG = "EBAT-";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");


    public static void d(String msg) {
        println(android.util.Log.DEBUG, getDefaultTag(), msg);
    }

    public static void i(String msg) {
        println(android.util.Log.INFO, getDefaultTag(), msg);
    }

    public static void e(String msg) {
        println(android.util.Log.ERROR, getDefaultTag(), msg);
    }


    public static void d(String tag, String msg) {
        println(android.util.Log.DEBUG, TAG + tag, msg);
    }


    public static void i(String tag, String msg) {
        println(android.util.Log.INFO, TAG + tag, msg);
    }


    public static void e(String tag, String msg) {
        println(android.util.Log.ERROR, TAG + tag, msg);
    }

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    public static void println(int priority, String tag, String msg) {
        if (logEnable) {
            byte[] bytes = msg.getBytes();
            int length = bytes.length;
            //由于log字数限制，当一条文本过长时，拆成多条信息log
            if (length > CHUNK_SIZE) {
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    int count = Math.min(length - i, CHUNK_SIZE);
                    android.util.Log.println(priority, tag, new String(bytes, i, count));
                }
            } else {
                android.util.Log.println(priority, tag, msg);
            }
        }
    }

    private static String getDefaultTag() {
        return TAG + getCallerStackTraceSimpleInfo();
    }


    private static String getCallerSimpleClassName() {
//        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
        StackTraceElement stackTraceElement = getCallerStackTrace();
        String result = stackTraceElement.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }


    private static String getCallerStackTraceSimpleInfo() {
        StackTraceElement stackTraceElement = getCallerStackTrace();
        String className = stackTraceElement.getClassName();
        String simpleClassname = className.substring(className.lastIndexOf(".") + 1, className.length());
        return stackTraceElement.toString().replace(className, simpleClassname);
    }


    private static StackTraceElement getCallerStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        boolean foundLClass = false;
        for (int i = 0; i < stackTrace.length; i++) {
            if (stackTrace[i].getClassName().equals(L.class.getName())) {
                foundLClass = true;
                continue;
            }
            if (foundLClass && !(stackTrace[i].getClassName().equals(L.class.getName()))) {
                return stackTrace[i];
            }
        }
        return null;
    }


    static {
        LogUtils.getLogConfig()
                .configAllowLog(logEnable)
                .configTagPrefix(TAG)
                .configShowBorders(true)
//                .configFormatTag(" %t %c")
                .configLevel(com.apkfuns.logutils.LogLevel.TYPE_VERBOSE);

        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(1)                 // default 2
                .logLevel(com.orhanobut.logger.LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(1);             // default 0

    }


    public static void log(Object o) {
        if (logEnable) {
            Logger.d(o);
        }

    }

    public static void log(String tag, Object o) {
        if (logEnable)
            Logger.t(tag).d(o);
    }


    public static synchronized void logIntent(String prefix, Intent intent) {
        if (logEnable) {
            LogUtils.getLogConfig().configFormatTag(prefix);
            LogUtils.d(intent == null ? "null" : intent);
            LogUtils.getLogConfig().configFormatTag(" ");
        }

    }

    public static synchronized void logBundle(String prefix, Bundle bundle) {
        if (logEnable) {
            LogUtils.getLogConfig().configFormatTag(prefix);
            LogUtils.d(bundle == null ? "null" : bundle);
            LogUtils.getLogConfig().configFormatTag(" ");
        }
    }

    public static void logJson(String json) {
        if (logEnable) {
            Logger.json(json);
        }
    }


}

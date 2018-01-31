package com.moriarty.base.web;

import android.webkit.WebView;

import com.moriarty.base.web.calljs.JavaCallJsUtil;
import com.moriarty.base.web.jscalljava.IJsCallMethod;
import com.moriarty.base.web.jscalljava.JsCallJavaUtil;

/**
 * Created by liuzhe on 2018/1/31.
 */

public class JSBridge {

    public static final long NO_CALLBACK = -1;


    /**
     * <p/>
     * 可供添加的方法说明：
     * 方法须为public static 的，且需用注解JsCallMethodAnnotation标注。
     * 如果要提供的方法名与实际所写的方法名不同，需要在JsCallMethodAnnotation中填上methodName属性，标明其要提供的方法名。
     * 方法参数固定为(WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier)这三个
     * 方法返回结果为BridgeResult类型，方法可抛出异常
     * 如果没有在注解JsCallMethodAnnotation中填上methodName属性，使用原方法名字注册时注意混淆时keep这些方法名
     * <p/>
     * 所需方法示例：
     * {@code
     *
     * @JsCallMethodAnnotation // @JsCallMethodAnnotation(methodName="methodName")
     * public static  Result  xxx  (WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier)  throws Throwable{
     * // do something
     * }
     * }
     * <p/>
     */

    public static void registerJsCallMethod(Class<?> clazz) {
        JsCallJavaUtil.register("bridge", clazz);
    }

    public static void registerJsCallMethod(IJsCallMethod jsCallMethod) {
        JsCallJavaUtil.register("bridge", jsCallMethod);
    }

    public static void unRegisterJsCallMethod(String methodName) {
        JsCallJavaUtil.unRegister("bridge", methodName);
    }


    public static BridgeResult jsCallJava(WebView webView, String jsBridgeData) {
        return JsCallJavaUtil.invoke(webView, jsBridgeData);
    }


    public static void javaCallJsByBridge(WebView webView, String methodName, String params, JavaCallJsUtil.Callback callback) {
        JavaCallJsUtil.javaCallJsByBridge(webView, methodName, params, callback);
    }

}

package com.moriarty.base.web.jscalljava;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.moriarty.base.web.BridgeResult;
import com.moriarty.base.web.JSBridge;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by liuzhe on 2018/1/31.
 */

public class JsCallJavaUtil {
    private static Map<String, Map<String, IJsCallMethod>> exposedMethods = new ArrayMap<>();


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


    public static void register(String objectName, Class<?> clazz) {
        List<IJsCallMethod> jsCallMethodList = null;
        try {
            jsCallMethodList = getJsCallMethodByClass(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != jsCallMethodList)
            for (IJsCallMethod jsCallMethod : jsCallMethodList) {
                register(objectName, jsCallMethod);
            }
    }

    public static void register(String objectName, IJsCallMethod jsCallMethod) {
        Map<String, IJsCallMethod> methodMap = null;
        if (!exposedMethods.containsKey(objectName)) {
            methodMap = new ArrayMap<>();
            methodMap.put(jsCallMethod.getMethodName(), jsCallMethod);
            exposedMethods.put(objectName, methodMap);
        } else {
            methodMap = exposedMethods.get(objectName);
            methodMap.put(jsCallMethod.getMethodName(), jsCallMethod);
        }
    }

    public static void unRegister(String objectName, IJsCallMethod jsCallMethod) {
        unRegister(objectName, jsCallMethod.getMethodName());
    }

    public static void unRegister(String objectName, String methodName) {
        if (exposedMethods.containsKey(objectName)) {
            Map<String, IJsCallMethod> methodMap = exposedMethods.get(objectName);
            if (methodMap.containsKey(methodName)) {
                methodMap.remove(methodName);
            }
        }
    }

    /**
     * 从一个类中获取可供js调用的方法
     * <p/>
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


    private static List<IJsCallMethod> getJsCallMethodByClass(Class injectedCls) throws Exception {
        List<IJsCallMethod> methodList = new ArrayList<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            JsCallMethodAnnotation jsCallMethodAnnotation = method.getAnnotation(JsCallMethodAnnotation.class);

            if (null == jsCallMethodAnnotation || method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)
                    || method.getReturnType() != BridgeResult.class
                    ) {
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (null != parameters && parameters.length == 3) {
                if (parameters[0] == WebView.class && parameters[1] == String.class && parameters[2] == JSCallbackApplier.class) {
                    ReflectJsCallMethod reflectJsCallMethod = new ReflectJsCallMethod(method);
                    methodList.add(reflectJsCallMethod);
                }
            }
        }
        return methodList;
    }


    public static BridgeResult invoke(WebView webView, String jsBridgeData) {


        String callObject, methodName, params, jsBridgeVersion;
        long callbackId;
        //uri数据解析
//        if (!TextUtils.isEmpty(jsBridgeData) && jsBridgeData.startsWith("JSBridge")) {
//            Uri uri = Uri.parse(jsBridgeData);
//            callObject = uri.getHost();
//            params = uri.getQuery();
//            callbackId = uri.getPort() + "";
//            String path = uri.getPath();
//            if (!TextUtils.isEmpty(path)) {
//                methodName = path.replace("/", "");
//            }
//        }

        //json格式数据解析
        // {callObject:obj,callMethod:method,params:params,callbackId:callbackId,jsBridgeVersion:version};
        try {
            Log.d("JSBridge", jsBridgeData);
            JSONObject jsonObject = new JSONObject(jsBridgeData);
            callObject = jsonObject.getString("callObject");
            methodName = jsonObject.getString("callMethod");
            params = jsonObject.optString("params");
            callbackId = jsonObject.optLong("callbackId", JSBridge.NO_CALLBACK);
            jsBridgeVersion = jsonObject.optString("jsBridgeVersion", null);
        } catch (JSONException e) {
            e.printStackTrace();
            return BridgeResult.NativeResult.error(BridgeResult.CODE_JS_BRIDGE_DATA_FORMAT_ERROR);
        }


        BridgeResult result;
        JSCallbackApplier jsCallbackApplier = null;


        if (JSBridge.NO_CALLBACK != callbackId) {
            jsCallbackApplier = new JSCallbackApplier(webView, String.valueOf(callbackId));
        }

        IJsCallMethod method = getMethod(callObject, methodName);
        if (method != null) {
            try {
                result = method.execute(webView, params, jsCallbackApplier);
            } catch (Throwable e) {
                e.printStackTrace();
                if (e instanceof ParamErrorException) {
                    //params不是正确的params
                    result = BridgeResult.NativeResult.error(BridgeResult.CODE_PARAMS_ERROR);
                } else {
                    //执行过程中发生异常
                    result = BridgeResult.NativeResult.error(BridgeResult.CODE_EXCEPTION);
                }
            }
        } else {
            //没有此方法
            result = BridgeResult.NativeResult.error(BridgeResult.CODE_METHOD_NOT_FOUND);
        }


        if (jsCallbackApplier != null && !jsCallbackApplier.isApplied() && result != null && (!"N300".equals(result.getCode()))) {
            jsCallbackApplier.apply(result);
        }
        return result;
    }


    private static IJsCallMethod getMethod(String callObject, String methodName) {
        if (exposedMethods.containsKey(callObject)) {
            Map<String, IJsCallMethod> methodMap = exposedMethods.get(callObject);
            if (methodMap != null && methodMap.size() != 0 && methodMap.containsKey(methodName)) {
                return methodMap.get(methodName);
            }
        }
        return null;
    }


    /**
     * 执行反射方法的JsCallMethod
     */
    private static class ReflectJsCallMethod implements IJsCallMethod {

        public ReflectJsCallMethod(Method method) {
            this.method = method;
        }

        Method method;


        @Override
        public String getMethodName() {
            JsCallMethodAnnotation jsCallMethodAnnotation = method.getAnnotation(JsCallMethodAnnotation.class);
            String methodName = jsCallMethodAnnotation.methodName();
            if (!TextUtils.isEmpty(methodName)) {
                return methodName;
            }
            return method.getName();
        }


        @Override
        public BridgeResult execute(WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier) throws Throwable {
            try {
                return (BridgeResult) method.invoke(null, webView, paramsJsonStr, jsCallbackApplier);
            } catch (InvocationTargetException e) {
//                e.printStackTrace();
                throw e.getTargetException();
            }
        }
    }


    /**
     * 解析从js传入的paramsJsonStr数据，在具体的method实现中调用此方法，将方法所需的必须参数和可选参数的参数名传入此方法中，
     * 此方法会从paramsJsonStr中检查并取出对应的参数名对应的值，如果paramsJsonStr未包含某个必须参数，则会抛出ParamErrorException
     *
     * @param paramsJsonStr      从js传入的params
     * @param necessaryParamKeys 方法所需必须的参数名
     * @param optionalParamKeys  方法所需可选的参数名
     * @return
     */

    public static Map<String, String> parseParamsJsonString(String paramsJsonStr, String[] necessaryParamKeys, String[] optionalParamKeys) {

        Map<String, String> paramsMap = new ArrayMap<>();
        try {
            JSONObject jsonObject = new JSONObject(paramsJsonStr);
            if (null != necessaryParamKeys)
                for (String paramKey : necessaryParamKeys) {
                    paramsMap.put(paramKey, jsonObject.getString(paramKey));
                }
            if (null != optionalParamKeys)
                for (String paramKey : optionalParamKeys) {
                    paramsMap.put(paramKey, jsonObject.optString(paramKey, null));
                }
        } catch (JSONException e) {
//            e.printStackTrace();
            throw new ParamErrorException("parse params error", e);
        }

        return paramsMap;
    }
}

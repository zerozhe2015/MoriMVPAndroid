package com.moriarty.base.web;

import android.support.v4.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by liuzhe on 2018/1/31.
 */

public class BridgeResult {
    /**
     * native 返回给js的code以N开头
     * js 返回给js的code以J开头
     * 200成功,
     * 250 传入的数据不是JsBridge数据格式,
     * 300异步处理,(执行某方法，某方法内需要异步处理，则在此方法的返回值为code 300的result，并在异步处理完成之后调用callback传递相应的result ）,
     * 400无此方法,
     * 500发生异常,
     * 501 传入的方法参数错误,
     * 其他根据业务参数来执行
     */

    public static final String CODE_SUCCESS = "200";
    public static final String CODE_JS_BRIDGE_DATA_FORMAT_ERROR = "250";
    public static final String CODE_ASYNC = "300";
    public static final String CODE_METHOD_NOT_FOUND = "400";
    public static final String CODE_EXCEPTION = "500";
    public static final String CODE_PARAMS_ERROR = "501";


    private static final Map<String, String> codeMessageMap = new ArrayMap<String, String>() {
        {
            put(CODE_SUCCESS, "OK");
            put(CODE_JS_BRIDGE_DATA_FORMAT_ERROR, "jsBridge data format error");
            put(CODE_ASYNC, "async execute");
            put(CODE_METHOD_NOT_FOUND, "method not found");
            put(CODE_EXCEPTION, "exception");
            put(CODE_PARAMS_ERROR, "params error");
        }
    };

    private String code;
    private String message;
    private String data;

    private BridgeResult() {
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        try {
            o.put("code", code);
            o.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (null == data) {
            try {
                o.put("data", JSONObject.NULL);
                return o;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            o.put("data", jsonObject);
            return o;
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(data);
            o.put("data", jsonArray);
            return o;
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        try {
            o.put("data", data);
            return o;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }


    public String toJsonString() {
        return toJson().toString();
    }


    /**
     * js方法执行返回的 Result
     */
    public static class JsResult {
        private static final String CODE_PREFIX = "J";

        public static BridgeResult parseJson(String json) {
            try {
                return parseJson(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("not result json data", e);
            }
        }

        public static BridgeResult parseJson(JSONObject jsonObject) {
            try {
                BridgeResult result = new BridgeResult();
                result.code = jsonObject.getString("code");
                result.message = jsonObject.optString("message");
                result.data = jsonObject.optString("data");
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("not result json data", e);
            }
        }

    }

    /**
     * js方法执行返回的 Result
     */
    public static class NativeResult {

        private static final String CODE_PREFIX = "N";

        private static String buildCode(String code) {

            return CODE_PREFIX + code;
        }

        public static BridgeResult success(String data) {
            BridgeResult result = new BridgeResult();
            result.code = buildCode(CODE_SUCCESS);
            result.message = codeMessageMap.get(CODE_SUCCESS);
            result.data = data;
            return result;
        }

        public static BridgeResult success(JSONObject data) {
            return success(data.toString());
        }


        public static BridgeResult error(String code, String message) {
            BridgeResult result = new BridgeResult();
            result.code = buildCode(code);
            result.message = message;
            return result;
        }

        public static BridgeResult error(String code) {
            return error(code, codeMessageMap.get(code));
        }

        public static BridgeResult async() {
            BridgeResult result = new BridgeResult();
            result.code = buildCode(CODE_ASYNC);
            result.message = codeMessageMap.get(CODE_ASYNC);
            return result;
        }

    }
}

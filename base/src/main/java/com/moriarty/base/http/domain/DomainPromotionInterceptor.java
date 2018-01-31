package com.moriarty.base.http.domain;

import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moriarty.base.encryption.DesUtils;
import com.moriarty.base.encryption.RopUtils;
import com.moriarty.base.http.CommonRequestParamsProvider;
import com.moriarty.base.http.HttpLogger;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class DomainPromotionInterceptor implements Interceptor {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final MediaType FROM_URL_ENCODED_MEDIATYPE = new FormBody.Builder().build().contentType();


    CommonRequestParamsProvider commonRequestParamsProvider;

    public DomainPromotionInterceptor(CommonRequestParamsProvider commonRequestParamsProvider) {
        this.commonRequestParamsProvider = commonRequestParamsProvider;
    }

    public DomainPromotionInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Gson gson = new Gson();
        Request request = chain.request();
        RequestBody body = request.body();

        Map<String, String> paramMap = getCommonRequestParams();    //new TreeMap<>(getCommonRequestParams());
        if (null != body) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            String requestBodyContent = buffer.readUtf8();
            if (!requestBodyContent.isEmpty()) {
                MediaType contentType = body.contentType();
                if (contentType.equals(FROM_URL_ENCODED_MEDIATYPE)) {
                    paramMap.putAll(getParamsMapFromFromUrlEncoded(requestBodyContent));

                } else if (contentType.equals(JSON_MEDIA_TYPE)) {
                    paramMap.putAll(gson.fromJson(requestBodyContent, new TypeToken<Map<String, String>>() {
                    }.getType()));
                } else {
                    throw new IllegalArgumentException("not support contentType " + contentType.toString());
                }
            }
        }

        String url = request.url().toString();
        int lastIndex = url.lastIndexOf("/");
        paramMap.put("method", url.substring(lastIndex + 1));
        url = url.substring(0, lastIndex);
        String finalRequestBodyContent = encryptRequestParams(paramMap);
        request = request.newBuilder().url(url).method(request.method(), RequestBody.create(JSON_MEDIA_TYPE, finalRequestBodyContent)).build();
        return chain.proceed(request);
    }


    /**
     * 从FromUrlEncoded格式的数据中获取map参数
     *
     * @param formData
     * @return
     */

    public static Map<String, String> getParamsMapFromFromUrlEncoded(String formData) {
        Map<String, String> params = new ArrayMap<>();
        String[] nameValuePairs = formData.split("&");
        for (String nameValuePair : nameValuePairs) {
            if (nameValuePair.contains("=")) {
                int equalSignIndex = nameValuePair.indexOf("=");
                String name = URLDecoder.decode(nameValuePair.substring(0, equalSignIndex));
                String value = "";
                if (equalSignIndex != nameValuePair.length() - 1) {
                    value = URLDecoder.decode(nameValuePair.substring(equalSignIndex + 1, nameValuePair.length()));
                }
                params.put(name, value);
            }
        }
        return params;
    }


    private static Map<String, String> staticommonParams;

    static {
        staticommonParams = new ArrayMap<>();
//        staticommonParams.put("appVersion",
//                "0.9.1"
////                PackageUtils.getCurrentVersionName()
//        );
//        staticommonParams.put("osType", "1");
//        staticommonParams.put("deviceIdf", DeviceInfoUtils.getDeviceId());
//        staticommonParams.put("osVersion", android.os.Build.VERSION.RELEASE);
//        staticommonParams.put("channelPath",
//                "2"//     PackageUtils.getChannel()
//        );
//        staticommonParams.put("methodVersion", "1.0");
        staticommonParams.put("appKey", "00001");
        staticommonParams.put("format", "json");
        staticommonParams.put("v", "1.0");
    }


    /**
     * 公共参数
     *
     * @return
     */
    public Map<String, String> getCommonRequestParams() {
        Map<String, String> commonParams = new ArrayMap<>();
        commonParams.putAll(staticommonParams);
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//        commonParams.put("timeStamp", timeStamp);
        if (commonRequestParamsProvider != null) {
            commonParams.putAll(commonRequestParamsProvider.provideCommonRequestParams());
        }
        return commonParams;
    }

    /**
     * 根据参数生成sign
     *
     * @param paramMap
     * @return
     */

    public static String generateSign(Map<String, String> paramMap) {
//        paramMap.remove("sign");
//        //先对params排序
//        SortedMap<String, String> sortedMap;
//
//        if (paramMap instanceof SortedMap) {
//            sortedMap = (SortedMap<String, String>) paramMap;
//        } else {
//            sortedMap = new TreeMap<>();
//            sortedMap.putAll(paramMap);
//        }
//
//        String privateKey = Keys.SIGN_MD5_KEY;
//        StringBuilder query = new StringBuilder(privateKey);
//        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
//            if (entry.getKey() != null && entry.getValue() != null) {
//                query.append(entry.getKey()).append(entry.getValue());
//            }
//        }
//
//        query.append(privateKey);
        String secret = "abcdeabcdeabcdeabcdeabcde";

        return RopUtils.sign(paramMap, secret);
    }


    /**
     * 将原始的请求参数加密成请求提交的密文
     *
     * @param originalRequestParams
     * @return
     */

    public static String encryptRequestParams(Map<String, String> originalRequestParams) {


        Map<String, String> params = new ArrayMap<>();


        params.putAll(originalRequestParams);
        //生成sign
        String sign = generateSign(originalRequestParams);
        //将sign放置在Param map中
        params.put("sign", sign);

//        new HttpLogger().log("params:" + params.toString());
        //生成json
        String json = new Gson().toJson(params, new TypeToken<Map<String, String>>() {
        }.getType());
        new HttpLogger().log("params:" + json);
        //加密json
        return DesUtils.encrypt(DesUtils.PROMOTION_CRYPT_KEY, json);


//        paramMap.put("sign", generateSign(paramMap));
//
//        String json = gson.toJson(paramMap, new TypeToken<Map<String, String>>() {
//        }.getType());
//
//        String finalRequestBodyContent = DesUtils.encrypt(json);

    }
}

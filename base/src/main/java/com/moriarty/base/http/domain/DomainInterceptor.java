package com.moriarty.base.http.domain;

import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moriarty.base.encryption.DesUtils;
import com.moriarty.base.encryption.Md5;
import com.moriarty.base.http.CommonRequestParamsProvider;
import com.moriarty.base.http.HttpLogger;
import com.moriarty.base.sc.Keys;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class DomainInterceptor implements Interceptor {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final MediaType FROM_URL_ENCODED_MEDIATYPE = new FormBody.Builder().build().contentType();


    CommonRequestParamsProvider commonRequestParamsProvider;

    public DomainInterceptor(CommonRequestParamsProvider commonRequestParamsProvider) {
        this.commonRequestParamsProvider = commonRequestParamsProvider;
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
//                    throw new IllegalArgumentException("not support contentType " + contentType.toString());

                    return chain.proceed(request);
                }
            }
        }
        String finalRequestBodyContent = encryptRequestParams(paramMap);
        request = request.newBuilder().method(request.method(), RequestBody.create(JSON_MEDIA_TYPE, finalRequestBodyContent)).build();
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


    /**
     * 公共参数
     *
     * @return
     */
    public Map<String, String> getCommonRequestParams() {
        Map<String, String> commonParams = new ArrayMap<>();
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
        paramMap.remove("sign");
        //先对params排序
        SortedMap<String, String> sortedMap;

        if (paramMap instanceof SortedMap) {
            sortedMap = (SortedMap<String, String>) paramMap;
        } else {
            sortedMap = new TreeMap<>();
            sortedMap.putAll(paramMap);
        }

        String privateKey = Keys.SIGN_MD5_KEY;
        StringBuilder query = new StringBuilder(privateKey);
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                query.append(entry.getKey()).append(entry.getValue());
            }
        }

        query.append(privateKey);
        return Md5.getMd5(query.toString());
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
        return DesUtils.encrypt(json);


//        paramMap.put("sign", generateSign(paramMap));
//
//        String json = gson.toJson(paramMap, new TypeToken<Map<String, String>>() {
//        }.getType());
//
//        String finalRequestBodyContent = DesUtils.encrypt(json);
    }


}

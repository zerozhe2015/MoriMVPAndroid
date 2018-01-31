/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.moriarty.base.http.domain;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 */
public final class DomainGsonConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static DomainGsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static DomainGsonConverterFactory create(Gson gson) {
        return new DomainGsonConverterFactory(gson);
    }

    private final Gson gson;

    private DomainGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {


        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DomainGsonResponseBodyConverter<>(gson, adapter, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }


    static final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }


    static final class DomainGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final Type type;


        DomainGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;

        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String responseBodystring = value.string();

            boolean specialHandling = false;
            if (TypeToken.get(type).getRawType() != DomainResult.class) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBodystring);
                    if (jsonObject.has("code")
//                            && jsonObject.has("message")
                            ) {
                        specialHandling = true;
                        DomainResult domainResult = new DomainResult();
                        domainResult.code = jsonObject.getString("code");
                        domainResult.message = jsonObject.optString("message", null);
                        if (domainResult.isSuccess()) {
                            if (jsonObject.has("data")) {
                                String dataJsonStr = jsonObject.getString("data");
                                if (null == dataJsonStr || dataJsonStr.isEmpty() || "null".equalsIgnoreCase(dataJsonStr)) {
                                    return null;
                                }
                                if (type == String.class) {
                                    return (T) dataJsonStr;
                                }
                                return adapter.fromJson(dataJsonStr);
                            } else {
                                return null;
                            }
                        } else {
//                            if (jsonObject.has("data")) {
                            domainResult.dataJsonStr = jsonObject.optString("data", null);
//                            }
                            throw new FailedResultError(responseBodystring, domainResult);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    specialHandling = false;
                }
            }


            try {
                if (!specialHandling) {
                    if (responseBodystring.isEmpty() || "null".equalsIgnoreCase(responseBodystring)) {
                        return null;
                    }

                    if (type == String.class) {
                        return (T) responseBodystring;
                    }
                    return adapter.fromJson(responseBodystring);
                }
            } finally {
                value.close();
            }
            return null;
        }
    }
}

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
package ext.arch.components.network.converter;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import ext.arch.components.annotations.InternalApi;
import ext.arch.components.model.HTTPResponse;
import ext.arch.components.model.Response;
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
public final class PolymGsonConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static PolymGsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static PolymGsonConverterFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new PolymGsonConverterFactory(gson);
    }

    private final Gson gson;

    private PolymGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        /*接口或抽象类*/
        if (getRawType(type).isInterface() || Modifier.isAbstract(getRawType(type).getModifiers())) {
            return new GsonRequestBodyConverter<>(gson);
        }
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (!isInternalApi(annotations) && !Response.class.isAssignableFrom(getRawType(type))) {
            return super.responseBodyConverter(type, annotations, retrofit);
        }
        return new GsonResponseBodyConverter<>(gson, type, annotations);
    }

    private static boolean isInternalApi(Annotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation instanceof InternalApi) {
                return true;
            }
        }
        return false;
    }

    static final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;

        GsonRequestBodyConverter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            gson.toJson(value, value.getClass(), jsonWriter);
            jsonWriter.flush();
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    static final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<?> adapter;
        private final boolean isInternalApi;
        private final boolean isResponseType;

        GsonResponseBodyConverter(Gson gson, Type type, Annotation[] annotations) {
            this.gson = gson;
            isInternalApi = isInternalApi(annotations);
            isResponseType = Response.class.isAssignableFrom(getRawType(type));
            if (isResponseType) {
                adapter = gson.getAdapter(TypeToken.get(type));
            } else if (isInternalApi) {
                final Class<? extends Response> responseType = getResponseType(annotations);
                adapter = gson.getAdapter(TypeToken.getParameterized(responseType, type));
            } else {
                // should not be here.
                adapter = gson.getAdapter(TypeToken.get(type));
            }
        }

        private static Class<? extends Response> getResponseType(Annotation[] annotations) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                if (annotation instanceof InternalApi) {
                    InternalApi internalApi = (InternalApi) annotation;
                    return internalApi.value();
                }
            }
            return HTTPResponse.class;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                Response response = (Response) adapter.read(jsonReader);
                if (response.isSuccess()) {
                    if (isResponseType) {
                        return (T) response;
                    } else if (isInternalApi) {
                        return (T) response.body();
                    } else {
                        return (T) response;
                    }
                } else {
                    throw response.error();
                }
            } finally {
                value.close();
            }
        }
    }
}

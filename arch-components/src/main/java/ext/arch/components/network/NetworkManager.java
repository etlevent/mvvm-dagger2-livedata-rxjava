package ext.arch.components.network;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ext.arch.components.json.NullSafeTypeAdapterFactory;
import ext.arch.components.network.converter.PolymGsonConverterFactory;
import ext.arch.components.util.Preconditions;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by roothost on 2018/1/4.
 */
public class NetworkManager {
    private static final String TAG = "NetworkManager";

    private static final String INTERNAL_URL_KEY = "header_url";
    public static final String HEADER_URL = INTERNAL_URL_KEY + ":";

    private static volatile NetworkManager _instance;
    private final Map<Class<?>, Object> mServiceMap;
    private final List<Interceptor> mInterceptors;
    private final List<Interceptor> mNetworkInterceptors;
    private final List<Converter.Factory> mConverterFactories;
    private final Map<String, HttpUrl> mDynamicUrlMap;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private String mBaseUrl;
    private File mCacheFile;

    private NetworkManager() {
        mServiceMap = new ArrayMap<>(20);
        mInterceptors = new ArrayList<>();
        mNetworkInterceptors = new ArrayList<>();
        mConverterFactories = new ArrayList<>();
        mDynamicUrlMap = new ArrayMap<>(10);
    }

    public static NetworkManager instance() {
        if (_instance == null) {
            synchronized (NetworkManager.class) {
                if (_instance == null) {
                    _instance = new NetworkManager();
                }
            }
        }
        return _instance;
    }

    public void baseUrl(@NonNull String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public void okHttpClient(@Nullable OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
    }

    public void addConverterFactory(@NonNull Converter.Factory factory) {
        Preconditions.checkNotNull(factory);
        if (!mConverterFactories.contains(factory)) {
            mConverterFactories.add(factory);
        }
    }

    public void addInterceptor(@NonNull Interceptor interceptor) {
        Preconditions.checkNotNull(interceptor);
        if (!mInterceptors.contains(interceptor)) {
            mInterceptors.add(interceptor);
        }
    }

    public void addNetworkInterceptor(@NonNull Interceptor interceptor) {
        Preconditions.checkNotNull(interceptor);
        if (!mNetworkInterceptors.contains(interceptor)) {
            mNetworkInterceptors.add(interceptor);
        }
    }

    public void putDynamicUrl(@NonNull String key, @Nullable String baseUrl) {
        Preconditions.checkNotNull(key);
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        if (httpUrl == null) {
            throw new IllegalArgumentException("invalid url. " + baseUrl);
        }
        mDynamicUrlMap.put(key, httpUrl);
    }

    @CheckResult
    @Nullable
    public HttpUrl getDynamicUrl(@NonNull String key) {
        Preconditions.checkNotNull(key);
        return mDynamicUrlMap.get(key);
    }

    public void cacheFile(@Nullable File cacheFile) {
        this.mCacheFile = cacheFile;
    }

    public void setGson(@Nullable Gson gson) {
        this.mGson = gson;
    }

    public <T> T getService(Class<T> service) {
        T realService = (T) mServiceMap.get(service);
        if (realService == null) {
            realService = createService(service);
            mServiceMap.put(service, realService);
        }
        return realService;
    }

    @NonNull
    private <T> T createService(Class<T> service) {
        if (TextUtils.isEmpty(mBaseUrl)) {
            throw new NullPointerException("base url must not be empty.");
        }
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder().build();
        }
        OkHttpClient.Builder clientBuilder = mOkHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor())
                .addInterceptor(cacheInterceptor())
                // finally add dynamic interceptor.
                .addInterceptor(dynamicInterceptor())
                .addNetworkInterceptor(rewriteCacheInterceptor());
        if (mCacheFile != null) {
            Cache cache = new Cache(mCacheFile, 20000000);
            clientBuilder.cache(cache);
        }
        for (Interceptor interceptor : mInterceptors) {
            clientBuilder.addInterceptor(interceptor);
        }
        for (Interceptor networkInterceptor : mNetworkInterceptors) {
            clientBuilder.addNetworkInterceptor(networkInterceptor);
        }
        /**
         * 使用忽略字段 excludeFieldsWithoutExposeAnnotation()
         * 所有需要json的实体字段需添加注解{@link com.google.gson.annotations.Expose}
         */
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .registerTypeAdapterFactory(new NullSafeTypeAdapterFactory())
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(PolymGsonConverterFactory.create(mGson))
                .addConverterFactory(GsonConverterFactory.create(mGson));
        for (Converter.Factory factory : mConverterFactories) {
            retrofitBuilder.addConverterFactory(factory);
        }
        final Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(service);
    }

    @NonNull
    private static Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @NonNull
    private static Interceptor cacheInterceptor() {
        return chain -> {
            Request newRequest = chain.request()
                    .newBuilder()
                    .addHeader("Cache-Control", "max-age=0")
                    .build();
            return chain.proceed(newRequest);
        };
    }

    @NonNull
    private static Interceptor rewriteCacheInterceptor() {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 2592000, 2592000))
                    .build();
        };
    }

    private Interceptor dynamicInterceptor() {
        return chain -> {
            final Request originalRequest = chain.request();
            final DynamicHeaderEntity dynamicHeaderEntity = getInternalUrlKey(originalRequest);
            if (dynamicHeaderEntity == null) {
                return chain.proceed(originalRequest);
            }
            final Request.Builder newRequestBuilder = originalRequest.newBuilder();
            final HttpUrl dynamicUrl = mDynamicUrlMap.get(dynamicHeaderEntity.key);
            final HttpUrl newUrl = replaceUrl(originalRequest.url(), dynamicUrl, dynamicHeaderEntity.segmentSize);
            newRequestBuilder.removeHeader(INTERNAL_URL_KEY)
                    .url(newUrl);
            return chain.proceed(newRequestBuilder.build());
        };
    }

    @Nullable
    private static DynamicHeaderEntity getInternalUrlKey(@NonNull Request request) {
        final List<String> urlHeaders = request.headers(INTERNAL_URL_KEY);
        if (urlHeaders == null || urlHeaders.isEmpty()) {
            return null;
        }
        if (urlHeaders.size() > 1) {
            throw new IllegalArgumentException("only allowed one header [" + INTERNAL_URL_KEY + "]");
        }
        if (TextUtils.isEmpty(urlHeaders.get(0))) {
            return null;
        }
        return new DynamicHeaderEntity(urlHeaders.get(0));
    }

    private static HttpUrl replaceUrl(HttpUrl originUrl, HttpUrl newUrl, int segmentSize) {
        // add changed path
        // remove path added by base api
        if (newUrl == null) {
            return originUrl;
        }
        final HttpUrl.Builder newUrlBuilder = originUrl.newBuilder()
                .scheme(newUrl.scheme())
                .host(newUrl.host())
                .port(newUrl.port());

        List<String> newPathSegments = newUrl.pathSegments();

        List<String> realPathSegments = new ArrayList<>(originUrl.pathSegments());
        for (int i = 0; i < segmentSize; i++) {
            realPathSegments.remove(0);
        }
        // add path to new url
        if (newPathSegments.size() > 0) {
            realPathSegments.addAll(0, newPathSegments);
        }
        // add all path to builder
        if (!realPathSegments.equals(originUrl.pathSegments())) {
            for (int i = 0; i < originUrl.pathSegments().size(); i++) {
                newUrlBuilder.removePathSegment(0);
            }
            for (String pathSegment : realPathSegments) {
                newUrlBuilder.addPathSegment(pathSegment);
            }
        }
        return newUrlBuilder.build();
    }

    private static class DynamicHeaderEntity {
        String key;
        int segmentSize;

        DynamicHeaderEntity(String source) {
            int index = source.indexOf("#");
            key = source.substring(0, index);
            segmentSize = Integer.valueOf(source.substring(index + 1, source.length()));
        }
    }
}

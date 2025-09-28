package com.example.movieexplorer.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Build OkHttpClient with timeouts and interceptors
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        // Add API key to every request
                        var originalRequest = chain.request();
                        var originalHttpUrl = originalRequest.url();

                        var url = originalHttpUrl.newBuilder()
                                .addQueryParameter(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY)
                                .addQueryParameter(ApiConstants.PARAM_LANGUAGE, ApiConstants.DEFAULT_LANGUAGE)
                                .build();

                        var requestBuilder = originalRequest.newBuilder().url(url);
                        var request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();
        }
        return okHttpClient;
    }

    public static void resetClient() {
        retrofit = null;
        okHttpClient = null;
    }
}
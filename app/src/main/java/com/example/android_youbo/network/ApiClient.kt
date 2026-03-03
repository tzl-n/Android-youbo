package com.example.android_youbo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit网络请求客户端
 */
object ApiClient {
    private const val BASE_URL = "http://10.161.9.80:7015/" // 实际 API 服务地址
        
    private const val TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNTI5ODMyNTUwNiIsImV4cCI6MTc1MDkwMjgwNX0.HZjIM4j9GXGkyO8D5j_En5pgYWnY__zONF6gSYUhnag"
        
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
        
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("token", TOKEN)
            .header("Accept", "*/*")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }
        
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
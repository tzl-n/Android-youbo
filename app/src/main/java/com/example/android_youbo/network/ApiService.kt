package com.example.android_youbo.network

import com.example.android_youbo.model.VideoApiResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * 示例 API 接口
 */
interface ApiService {

    /**
     * 测试连接 - 获取 API 健康状态
     */
    @GET("health")
    suspend fun getHealthStatus(): Response<ApiResponse<String>>

    /**
     * 获取商品列表
     */
    @GET("api/products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<List<Product>>>

    /**
     * 获取商品详情
     */
    @GET("api/products/{id}")
    suspend fun getProductDetail(@Path("id") productId: Long): Response<ApiResponse<Product>>

    /**
     * 搜索商品
     */
    @GET("api/products/search")
    suspend fun searchProducts(@Query("keyword") keyword: String): Response<ApiResponse<List<Product>>>

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ApiResponse<User>>

    /**
     * 获取用户信息
     */
    @GET("api/user/info")
    suspend fun getUserInfo(): Response<ApiResponse<User>>

    /**
     * 获取推荐短视频列表
     */
    @GET("videosimple/getRecommendSimpleVideo")
    suspend fun getRecommendVideos(
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20
    ): Response<VideoApiResponse>
}

/**
 * 商品数据类
 */
data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String
)

/**
 * 用户数据类
 */
data class User(
    val id: Long,
    val username: String,
    val nickname: String,
    val avatar: String
)

package com.example.android_youbo.network

/**
 * 网络请求结果包装类
 */
sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean = true) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val code: Int = -1) : NetworkResult<T>()
    data class Empty<T>(val message: String = "暂无数据") : NetworkResult<T>()
}
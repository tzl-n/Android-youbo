package com.example.android_youbo.network

/**
 * 网络请求响应包装类
 */
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
) {
    companion object {
        const val CODE_SUCCESS = 200
        const val CODE_ERROR = 500
        const val CODE_UNAUTHORIZED = 401
    }
    
    fun isSuccess(): Boolean = code == CODE_SUCCESS
    
    fun isError(): Boolean = !isSuccess()
}
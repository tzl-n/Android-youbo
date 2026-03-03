package com.example.android_youbo.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * 网络请求扩展函数
 */
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> Response<ApiResponse<T>>): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {
                        val data = apiResponse.data
                        if (data != null) {
                            NetworkResult.Success(data)
                        } else {
                            NetworkResult.Empty()
                        }
                    } else {
                        NetworkResult.Error(apiResponse.message, apiResponse.code)
                    }
                } else {
                    NetworkResult.Error("服务器返回空数据", ApiResponse.CODE_ERROR)
                }
            } else {
                NetworkResult.Error("网络请求失败: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "未知错误", -1)
        }
    }
}

/**
 * 处理网络结果的扩展函数
 */
fun <T : Any> NetworkResult<T>.handleResult(
    onLoading: ((Boolean) -> Unit)? = null,
    onSuccess: ((T) -> Unit)? = null,
    onError: ((String, Int) -> Unit)? = null,
    onEmpty: (() -> Unit)? = null
) {
    when (this) {
        is NetworkResult.Loading -> onLoading?.invoke(isLoading)
        is NetworkResult.Success -> onSuccess?.invoke(data)
        is NetworkResult.Error -> onError?.invoke(message, code)
        is NetworkResult.Empty -> onEmpty?.invoke()
    }
}
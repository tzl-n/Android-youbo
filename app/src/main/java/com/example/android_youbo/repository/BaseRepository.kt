package com.example.android_youbo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_youbo.network.ApiClient
import com.example.android_youbo.network.ApiService
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.network.safeApiCall

/**
 * Repository基类
 */
abstract class BaseRepository {
    protected val apiService: ApiService = ApiClient.createService()
    
    /**
     * 处理网络请求结果并更新LiveData
     */
    protected fun <T : Any> handleNetworkResult(
        result: NetworkResult<T>,
        liveData: MutableLiveData<NetworkResult<T>>
    ) {
        liveData.value = result
    }
    
    /**
     * 执行安全的网络请求
     */
    protected suspend fun <T : Any> safeApiRequest(
        apiCall: suspend () -> retrofit2.Response<com.example.android_youbo.network.ApiResponse<T>>
    ): NetworkResult<T> {
        return safeApiCall(apiCall)
    }
}
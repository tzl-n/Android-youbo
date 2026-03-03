package com.example.android_youbo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_youbo.network.NetworkResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * ViewModel基类
 */
abstract class BaseViewModel : ViewModel() {
    
    /**
     * 协程异常处理器
     */
    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // 统一处理协程异常
        handleError(throwable.message ?: "未知错误")
    }
    
    /**
     * 安全执行协程任务【
     */
    protected fun safeLaunch(block: suspend () -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            block()
        }
    }
    
    /**
     * 处理错误
     */
    protected open fun handleError(message: String) {
        // 子类可以重写此方法来处理特定错误
    }
    
    /**
     * 处理网络结果的通用方法
     */
    protected fun <T : Any> handleNetworkResult(
        result: NetworkResult<T>,
        onLoading: ((Boolean) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((String, Int) -> Unit)? = null,
        onEmpty: (() -> Unit)? = null
    ) {
        when (result) {
            is NetworkResult.Loading -> onLoading?.invoke(result.isLoading)
            is NetworkResult.Success -> onSuccess?.invoke(result.data)
            is NetworkResult.Error -> onError?.invoke(result.message, result.code)
            is NetworkResult.Empty -> onEmpty?.invoke()
        }
    }
}
package com.example.android_youbo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_youbo.network.LoginRequest
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.network.User

/**
 * 用户Repository
 */
class UserRepository : BaseRepository() {
    
    private val _loginResult = MutableLiveData<NetworkResult<User>>()
    val loginResult: LiveData<NetworkResult<User>> = _loginResult
    
    private val _userInfo = MutableLiveData<NetworkResult<User>>()
    val userInfo: LiveData<NetworkResult<User>> = _userInfo
    
    /**
     * 用户登录
     */
    suspend fun login(username: String, password: String) {
        _loginResult.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.login(LoginRequest(username, password))
        }
        
        handleNetworkResult(result, _loginResult)
    }
    
    /**
     * 获取用户信息
     */
    suspend fun getUserInfo() {
        _userInfo.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.getUserInfo()
        }
        
        handleNetworkResult(result, _userInfo)
    }
}
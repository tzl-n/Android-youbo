package com.example.android_youbo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.network.User
import com.example.android_youbo.repository.UserRepository

/**
 * 用户 ViewModel
 */
class UserViewModel : BaseViewModel() {

    private val repository = UserRepository()

    // 登录结果
    val loginResult: LiveData<NetworkResult<User>> = repository.loginResult

    // 用户信息
    val userInfo: LiveData<NetworkResult<User>> = repository.userInfo

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * 用户登录
     */
    fun login(username: String, password: String) {
        _loading.value = true
        safeLaunch {
            repository.login(username, password)
        }
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        _loading.value = true
        safeLaunch {
            repository.getUserInfo()
        }
    }

    /**
     * 登出
     */
    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    /**
     * 检查登录状态
     */
    fun checkLoginStatus() {
        // 这里可以从 SharedPreferences 或数据库检查登录状态
        // 示例：假设已登录
        _isLoggedIn.value = false
    }

    override fun handleError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }
}

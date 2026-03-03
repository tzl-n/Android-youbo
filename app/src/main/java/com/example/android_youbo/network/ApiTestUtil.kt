package com.example.android_youbo.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * API测试工具类
 * 用于测试API连接和基本功能
 */
object ApiTestUtil {
    
    /**
     * 检查网络连接状态
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    /**
     * 获取网络类型
     */
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return "无网络"
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "未知"
        
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "移动数据"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "以太网"
            else -> "其他网络"
        }
    }
    
    private val apiService = ApiClient.createService<ApiService>()
    
    /**
     * 测试API连接状态（基础版本）
     */
    fun testConnection(scope: CoroutineScope, callback: (Boolean, String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getHealthStatus()
                if (response.isSuccessful) {
                    val result = response.body()
                    callback(true, "连接成功: ${result?.message ?: "服务正常"}")
                } else {
                    callback(false, "连接失败: HTTP ${response.code()}")
                }
            } catch (e: HttpException) {
                callback(false, "HTTP错误: ${e.message()}")
            } catch (e: IOException) {
                callback(false, "网络错误: ${e.message}")
            } catch (e: Exception) {
                callback(false, "未知错误: ${e.message}")
            }
        }
    }
    
    /**
     * 测试API连接状态（带网络检查版本）
     */
    fun testConnectionWithNetworkCheck(context: Context, scope: CoroutineScope, callback: (Boolean, String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            // 先检查网络状态
            if (!isNetworkAvailable(context)) {
                callback(false, "网络不可用，请检查网络连接")
                return@launch
            }
            
            val networkType = getNetworkType(context)
            
            try {
                val response = apiService.getHealthStatus()
                if (response.isSuccessful) {
                    val result = response.body()
                    callback(true, "[${networkType}] 连接成功: ${result?.message ?: "服务正常"}")
                } else {
                    callback(false, "[${networkType}] 连接失败: HTTP ${response.code()}")
                }
            } catch (e: HttpException) {
                callback(false, "[${networkType}] HTTP错误: ${e.message()}")
            } catch (e: IOException) {
                callback(false, "[${networkType}] 网络错误: ${e.message}")
            } catch (e: Exception) {
                callback(false, "[${networkType}] 未知错误: ${e.message}")
            }
        }
    }
    
    /**
     * 测试获取商品列表
     */
    fun testGetProducts(scope: CoroutineScope, callback: (Boolean, String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getProducts(1, 10)
                if (response.isSuccessful) {
                    val products = response.body()?.data
                    callback(true, "获取商品成功，共${products?.size ?: 0}个商品")
                } else {
                    callback(false, "获取商品失败: HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                callback(false, "获取商品异常: ${e.message}")
            }
        }
    }
}
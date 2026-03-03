package com.example.android_youbo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.network.Product
import com.example.android_youbo.repository.ProductRepository

/**
 * 商品ViewModel
 */
class ProductViewModel : BaseViewModel() {
    
    private val repository = ProductRepository()
    
    // 商品列表
    val products: LiveData<NetworkResult<List<Product>>> = repository.products
    
    // 商品详情
    val productDetail: LiveData<NetworkResult<Product>> = repository.productDetail
    
    // 搜索结果
    val searchResults: LiveData<NetworkResult<List<Product>>> = repository.searchResults
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    /**
     * 获取商品列表
     */
    fun getProducts(page: Int = 1, size: Int = 20) {
        safeLaunch {
            repository.getProducts(page, size)
        }
    }
    
    /**
     * 获取商品详情
     */
    fun getProductDetail(productId: Long) {
        safeLaunch {
            repository.getProductDetail(productId)
        }
    }
    
    /**
     * 搜索商品
     */
    fun searchProducts(keyword: String) {
        safeLaunch {
            repository.searchProducts(keyword)
        }
    }
    
    /**
     * 刷新商品列表
     */
    fun refreshProducts() {
        getProducts(1, 20)
    }
    
    override fun handleError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }
}
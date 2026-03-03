package com.example.android_youbo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.network.Product

/**
 * 商品Repository
 */
class ProductRepository : BaseRepository() {
    
    private val _products = MutableLiveData<NetworkResult<List<Product>>>()
    val products: LiveData<NetworkResult<List<Product>>> = _products
    
    private val _productDetail = MutableLiveData<NetworkResult<Product>>()
    val productDetail: LiveData<NetworkResult<Product>> = _productDetail
    
    private val _searchResults = MutableLiveData<NetworkResult<List<Product>>>()
    val searchResults: LiveData<NetworkResult<List<Product>>> = _searchResults
    
    /**
     * 获取商品列表
     */
    suspend fun getProducts(page: Int = 1, size: Int = 20) {
        _products.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.getProducts(page, size)
        }
        
        handleNetworkResult(result, _products)
    }
    
    /**
     * 获取商品详情
     */
    suspend fun getProductDetail(productId: Long) {
        _productDetail.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.getProductDetail(productId)
        }
        
        handleNetworkResult(result, _productDetail)
    }
    
    /**
     * 搜索商品
     */
    suspend fun searchProducts(keyword: String) {
        _searchResults.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.searchProducts(keyword)
        }
        
        handleNetworkResult(result, _searchResults)
    }
}
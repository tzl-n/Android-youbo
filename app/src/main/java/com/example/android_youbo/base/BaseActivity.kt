package com.example.android_youbo.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_youbo.viewmodel.BaseViewModel

/**
 * Activity基类
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    
    protected lateinit var mViewModel: VM
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 初始化ViewModel
        mViewModel = createViewModel()
        
        // 初始化视图
        initView()
        
        // 初始化数据
        initData()
        
        // 初始化监听器
        initListener()
    }
    
    /**
     * 创建ViewModel实例
     */
    protected abstract fun createViewModel(): VM
    
    /**
     * 初始化视图
     */
    protected open fun initView() {}
    
    /**
     * 初始化数据
     */
    protected open fun initData() {}
    
    /**
     * 初始化监听器
     */
    protected open fun initListener() {}
    
    /**
     * 显示Toast消息
     */
    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    /**
     * 显示加载状态
     */
    protected open fun showLoading() {}
    
    /**
     * 隐藏加载状态
     */
    protected open fun hideLoading() {}
    
    /**
     * 处理错误
     */
    protected open fun handleError(message: String) {
        showToast(message)
    }
}
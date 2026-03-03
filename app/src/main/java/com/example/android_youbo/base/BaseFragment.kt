package com.example.android_youbo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android_youbo.viewmodel.BaseViewModel

/**
 * Fragment基类
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    
    protected lateinit var mViewModel: VM
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
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
     * 获取布局ID
     */
    protected abstract fun getLayoutId(): Int
    
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
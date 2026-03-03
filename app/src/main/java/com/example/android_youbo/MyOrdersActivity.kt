package com.example.android_youbo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MyOrdersActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTabAll: TextView
    private lateinit var tvTabPendingPayment: TextView
    private lateinit var tvTabPendingShipment: TextView
    private lateinit var tvTabPendingReceipt: TextView
    private lateinit var tvTabPendingReview: TextView
    private lateinit var llEmpty: View
    private lateinit var fragmentContainer: ViewGroup

    // 当前选中的 Tab
    private var currentTab = 0 // 默认选中"全部"
    
    // Fragment 管理
    private val fragmentManager: FragmentManager by lazy {
        supportFragmentManager
    }
    
    // Fragment 实例缓存
    private val fragments = mutableMapOf<Int, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        // 获取传入的订单类型
        val orderType = intent.getIntExtra("ORDER_TYPE", 0)

        initViews()
        setupClickListeners()
        selectTab(orderType)
    }

    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        tvTabAll = findViewById(R.id.tvTabAll)
        tvTabPendingPayment = findViewById(R.id.tvTabPendingPayment)
        tvTabPendingShipment = findViewById(R.id.tvTabPendingShipment)
        tvTabPendingReceipt = findViewById(R.id.tvTabPendingReceipt)
        tvTabPendingReview = findViewById(R.id.tvTabPendingReview)
        llEmpty = findViewById(R.id.llEmpty)
        fragmentContainer = findViewById(R.id.fragmentContainer)
    }

    private fun setupClickListeners() {
        // 返回按钮
        ivBack.setOnClickListener {
            finish()
        }

        // Tab 点击事件
        tvTabAll.setOnClickListener {
            selectTab(0)
        }

        tvTabPendingPayment.setOnClickListener {
            selectTab(1)
        }

        tvTabPendingShipment.setOnClickListener {
            selectTab(2)
        }

        tvTabPendingReceipt.setOnClickListener {
            selectTab(3)
        }

        tvTabPendingReview.setOnClickListener {
            selectTab(4)
        }
    }

    private fun selectTab(tabIndex: Int) {
        if (currentTab == tabIndex) {
            return // 如果点击的是当前选中的 Tab，不做处理
        }
        
        currentTab = tabIndex

        // 重置所有 Tab 样式
        resetTabs()

        // 设置选中 Tab 样式
        when (tabIndex) {
            0 -> {
                tvTabAll.setTextColor(resources.getColor(android.R.color.black))
                tvTabAll.paint.isFakeBoldText = true
            }
            1 -> {
                tvTabPendingPayment.setTextColor(resources.getColor(android.R.color.black))
                tvTabPendingPayment.paint.isFakeBoldText = true
            }
            2 -> {
                tvTabPendingShipment.setTextColor(resources.getColor(android.R.color.black))
                tvTabPendingShipment.paint.isFakeBoldText = true
            }
            3 -> {
                tvTabPendingReceipt.setTextColor(resources.getColor(android.R.color.black))
                tvTabPendingReceipt.paint.isFakeBoldText = true
            }
            4 -> {
                tvTabPendingReview.setTextColor(resources.getColor(android.R.color.black))
                tvTabPendingReview.paint.isFakeBoldText = true
            }
        }

        // 切换 Fragment
        switchFragment(tabIndex)
    }

    private fun resetTabs() {
        val defaultColor = resources.getColor(android.R.color.black)
        
        tvTabAll.setTextColor(defaultColor)
        tvTabAll.paint.isFakeBoldText = false

        tvTabPendingPayment.setTextColor(defaultColor)
        tvTabPendingPayment.paint.isFakeBoldText = false

        tvTabPendingShipment.setTextColor(defaultColor)
        tvTabPendingShipment.paint.isFakeBoldText = false

        tvTabPendingReceipt.setTextColor(defaultColor)
        tvTabPendingReceipt.paint.isFakeBoldText = false

        tvTabPendingReview.setTextColor(defaultColor)
        tvTabPendingReview.paint.isFakeBoldText = false
    }

    /**
     * 切换 Fragment
     */
    private fun switchFragment(tabIndex: Int) {
        // 隐藏空视图
        llEmpty.visibility = View.GONE
        
        // 获取或创建 Fragment
        val fragment = getOrCreateFragment(tabIndex)
        
        // 执行 Fragment 事务
        fragmentManager.beginTransaction().apply {
            // 隐藏所有 Fragment
            fragments.values.forEach {
                hide(it)
            }
            
            // 显示或添加当前 Fragment
            if (fragment.isAdded) {
                show(fragment)
            } else {
                add(R.id.fragmentContainer, fragment, "fragment_$tabIndex")
            }
            
            // 提交事务
            commitAllowingStateLoss()
        }
    }
    
    /**
     * 获取或创建 Fragment
     */
    private fun getOrCreateFragment(orderType: Int): Fragment {
        return fragments.getOrPut(orderType) {
            OrderListFragment.newInstance(orderType)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        fragments.clear()
    }
}

package com.example.android_youbo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class                                                    MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // 初始化底部导航栏
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        // 设置默认选中项
        bottomNavigation.selectedItemId = R.id.action_youbo
        
        // 设置导航点击事件
        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.action_youbo -> YouboFragment()
                R.id.action_haohuo -> HaohuoFragment()
                R.id.action_message -> MessageFragment()
                R.id.action_mine -> MineFragment()
                else -> NullFragment()
            }
            
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            
            true
        }
        
        // 初始加载第一个Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, YouboFragment())
            .commit()
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
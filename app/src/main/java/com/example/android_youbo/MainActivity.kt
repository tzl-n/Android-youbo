package com.example.android_youbo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android_youbo.network.ApiTestUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class                                                    MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var btnTestConnection: Button
    private lateinit var btnTestProducts: Button
    private lateinit var tvTestResult: TextView
    
    // SharedPreferences 相关
    private val PREFS_NAME = "user_prefs"
    private val KEY_TEEN_MODE_ACKNOWLEDGED = "teen_mode_acknowledged"
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // 初始化 SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        // 检查是否需要显示青少年模式提示
        showTeenModeDialogIfNeeded()
        
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
    
    /**
     * 显示青少年模式提示弹窗（如果需要）
     */
    private fun showTeenModeDialogIfNeeded() {
        val isAcknowledged = prefs.getBoolean(KEY_TEEN_MODE_ACKNOWLEDGED, false)
        
        if (!isAcknowledged) {
            showTeenModeDialog()
        }
    }
    
    /**
     * 显示青少年模式弹窗
     */
    private fun showTeenModeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_teen_mode, null)
        
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        // 设置"我知道了"按钮点击事件
        dialogView.findViewById<Button>(R.id.btn_i_know).setOnClickListener {
            // 保存已确认状态
            prefs.edit().putBoolean(KEY_TEEN_MODE_ACKNOWLEDGED, true).apply()
            dialog.dismiss()
        }
        
        // 设置"设置青少年模式"按钮点击事件
        dialogView.findViewById<TextView>(R.id.tv_setup_teen_mode).setOnClickListener {
            // 跳转到青少年模式页面
            val intent = Intent(this@MainActivity, TeenModeActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        
        // 防止用户点击外部关闭对话框
        dialog.setCancelable(false)
        
        dialog.show()
    }
}
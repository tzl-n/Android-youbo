package com.example.android_youbo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    
    // 引导页显示时间（毫秒）
    private val SPLASH_TIME_OUT = 1000L
    
    // SharedPreferences 名称
    private val PREFS_NAME = "user_prefs"
    
    // 用户是否同意隐私政策的 key
    private val KEY_AGREEMENT_ACCEPTED = "agreement_accepted"
    
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // 初始化 SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        // 检查用户是否已经同意过隐私政策
        val isAccepted = prefs.getBoolean(KEY_AGREEMENT_ACCEPTED, false)
        
        if (isAccepted) {
            // 用户已同意，直接进入主界面
            startSplashTimer()
        } else {
            // 用户未同意，显示协议弹窗
            showUserAgreementDialog()
        }
    }
    
    /**
     * 显示用户协议和隐私政策弹窗
     */
    private fun showUserAgreementDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_agreement, null)
        
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        // 设置按钮点击事件
        dialogView.findViewById<Button>(R.id.btn_disagree).setOnClickListener {
            // 不同意并退出
            finish()
        }
        
        dialogView.findViewById<Button>(R.id.btn_agree).setOnClickListener {
            // 同意并继续
            // 保存用户同意状态
            prefs.edit().putBoolean(KEY_AGREEMENT_ACCEPTED, true).apply()
            
            dialog.dismiss()
            startSplashTimer()
        }
        
        // 防止用户点击外部关闭对话框
        dialog.setCancelable(false)
        
        dialog.show()
    }
    
    /**
     * 开始引导页倒计时
     */
    private fun startSplashTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // 关闭引导页，防止用户返回
        }, SPLASH_TIME_OUT)
    }
}
package com.example.android_youbo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.android_youbo.base.BaseActivity
import com.example.android_youbo.databinding.ActivityLoginBinding
import com.example.android_youbo.network.NetworkResult
import com.example.android_youbo.viewmodel.UserViewModel

/**
 * 登录页面
 */
class LoginActivity : BaseActivity<UserViewModel>() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 先初始化 binding
        Log.d(TAG, "onCreate called")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 再调用父类 onCreate
        super.onCreate(savedInstanceState)
    }

    override fun createViewModel(): UserViewModel {
        Log.d(TAG, "Creating UserViewModel")
        return UserViewModel()
    }

    override fun initView() {
        Log.d(TAG, "Initializing view")
        window.statusBarColor = Color.WHITE
        binding.btnLogin.isEnabled = true
    }

    override fun initData() {
        Log.d(TAG, "Initializing data")

        mViewModel.loginResult.observe(this, Observer { result ->
            Log.d(TAG, "Login result observed: $result")
            when (result) {
                is NetworkResult.Loading -> {
                    Log.d(TAG, "Loading state")
                    showLoading()
                }
                is NetworkResult.Success -> {
                    Log.d(TAG, "Success state")
                    hideLoading()
                    val user = result.data
                    Toast.makeText(this, "登录成功！欢迎 ${user.nickname}", Toast.LENGTH_SHORT).show()

                    saveUserInfo(user)
                    navigateToMain()
                    finish()
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "Error state: ${result.message}")
                    hideLoading()
                    Toast.makeText(this, "登录失败：${result.message}", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Empty -> {
                    Log.d(TAG, "Empty state: ${result.message}")
                    hideLoading()
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        mViewModel.errorMessage.observe(this, Observer { message ->
            Log.e(TAG, "Error message observed: $message")
            hideLoading()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun initListener() {
        Log.d(TAG, "Initializing listeners")

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            Log.d(TAG, "Login button clicked, username: $username")

            if (validateInput(username, password)) {
                Log.d(TAG, "Input validated, calling login")
                mViewModel.login(username, password)
            } else {
                Log.e(TAG, "Input validation failed")
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Log.d(TAG, "Forgot password clicked")
            Toast.makeText(this, "请联系客服重置密码", Toast.LENGTH_SHORT).show()
        }

        binding.tvRegister.setOnClickListener {
            Log.d(TAG, "Register clicked")
            Toast.makeText(this, "注册功能开发中...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.tilUsername.error = "请输入用户名"
            binding.etUsername.requestFocus()
            Log.w(TAG, "Validation failed: username empty")
            return false
        }

        if (username.length < 3) {
            binding.tilUsername.error = "用户名至少 3 个字符"
            binding.etUsername.requestFocus()
            Log.w(TAG, "Validation failed: username too short")
            return false
        }

        binding.tilUsername.error = null

        if (password.isEmpty()) {
            binding.tilPassword.error = "请输入密码"
            binding.etPassword.requestFocus()
            Log.w(TAG, "Validation failed: password empty")
            return false
        }

        if (password.length < 6) {
            binding.tilPassword.error = "密码至少 6 个字符"
            binding.etPassword.requestFocus()
            Log.w(TAG, "Validation failed: password too short")
            return false
        }

        binding.tilPassword.error = null
        Log.d(TAG, "Validation successful")
        return true
    }

    private fun saveUserInfo(user: com.example.android_youbo.network.User) {
        Log.d(TAG, "Saving user info: ${user.username}")
        val prefs = getSharedPreferences("user_info", MODE_PRIVATE)
        prefs.edit().apply {
            putLong("user_id", user.id)
            putString("username", user.username)
            putString("nickname", user.nickname)
            putString("avatar", user.avatar)
            putBoolean("is_logged_in", true)
            apply()
            Log.d(TAG, "User info saved successfully")
        }
    }

    private fun navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        Log.d(TAG, "Showing loading state")
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "登录中..."
    }

    override fun hideLoading() {
        Log.d(TAG, "Hiding loading state")
        binding.btnLogin.isEnabled = true
        binding.btnLogin.text = "登录"
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}

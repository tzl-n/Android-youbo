package com.example.android_youbo

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android_youbo.databinding.FragmentMineBinding

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        checkLoginStatus()
    }

    /**
     * 检查登录状态
     */
    private fun checkLoginStatus() {
        val prefs = requireContext().getSharedPreferences("user_info", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            // 未登录，跳转到登录页面
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            // 已登录，正常显示我的页面
            showMineContent()
        }
    }

    /**
     * 显示我的页面内容
     */
    private fun showMineContent() {
        val prefs = requireContext().getSharedPreferences("user_info", MODE_PRIVATE)
        val nickname = prefs.getString("nickname", "用户")

        // 设置用户昵称
        binding.tvTitle.text = "欢迎，$nickname"
    }

    override fun onResume() {
        super.onResume()
        // 从登录页返回时重新检查登录状态
        checkLoginStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

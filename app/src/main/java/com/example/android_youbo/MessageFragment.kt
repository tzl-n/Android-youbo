package com.example.android_youbo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android_youbo.message.MyMessageActivity
import com.example.android_youbo.message.MessOrdersActivity

class MessageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 查找订单交易视图并设置点击事件
        view.findViewById<View>(R.id.llOrder).setOnClickListener {
            val intent = Intent(requireContext(), MessOrdersActivity::class.java)
            startActivity(intent)
        }

        // 查找互动消息视图并设置点击事件
        view.findViewById<View>(R.id.llInteraction).setOnClickListener {
            val intent = Intent(requireContext(), MyMessageActivity::class.java)
            startActivity(intent)
        }
    }
}
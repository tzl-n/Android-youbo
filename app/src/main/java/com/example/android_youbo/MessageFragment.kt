package com.example.android_youbo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.android_youbo.message.MyMessageActivity

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

        // 订单交易点击事件
        view.findViewById<LinearLayout>(R.id.llOrder).setOnClickListener {
            startActivity(Intent(requireContext(), com.example.android_youbo.message.MessageOderActivity::class.java))
        }

        // 互动消息点击事件
        view.findViewById<LinearLayout>(R.id.llInteraction).setOnClickListener {
            startActivity(Intent(requireContext(), MyMessageActivity::class.java))
        }
    }
}
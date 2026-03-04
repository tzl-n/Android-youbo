package com.example.android_youbo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class OrderListFragment : Fragment() {

    companion object {
        const val ARG_ORDER_TYPE = "order_type"

        fun newInstance(orderType: Int): OrderListFragment {
            val fragment = OrderListFragment()
            val args = Bundle()
            args.putInt(ARG_ORDER_TYPE, orderType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val orderType = arguments?.getInt(ARG_ORDER_TYPE, 0) ?: 0
        val tvEmptyText = view.findViewById<TextView>(R.id.tvEmptyText)
        
        // 根据订单类型设置不同的提示文字
        val orderTypeName = when (orderType) {
            0 -> "全部"
            1 -> "待付款"
            2 -> "待发货"
            3 -> "待收货"
            4 -> "待评价"
            else -> "订单"
        }
        
        tvEmptyText.text = "暂无$orderTypeName 订单哦~"
    }
}

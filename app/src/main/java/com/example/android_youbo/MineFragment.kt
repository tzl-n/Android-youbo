package com.example.android_youbo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class MineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        // 横幅按钮点击
        view.findViewById<Button>(R.id.btnActivate).setOnClickListener {
            Toast.makeText(context, "点击开通", Toast.LENGTH_SHORT).show()
        }

        // 查看全部订单
        view.findViewById<TextView>(R.id.tvViewAll).setOnClickListener {
            navigateToOrders(view, 0) // 0 表示全部订单
        }

        // 订单相关点击
        view.findViewById<ImageView>(R.id.ivPendingPayment).setOnClickListener {
            navigateToOrders(view, 1) // 1 表示待付款
        }
        view.findViewById<ImageView>(R.id.ivPendingShipment).setOnClickListener {
            navigateToOrders(view, 2) // 2 表示待发货
        }
        view.findViewById<ImageView>(R.id.ivPendingReceipt).setOnClickListener {
            navigateToOrders(view, 3) // 3 表示待收货
        }
        view.findViewById<ImageView>(R.id.ivPendingReview).setOnClickListener {
            navigateToOrders(view, 4) // 4 表示待评价
        }
        view.findViewById<ImageView>(R.id.ivRefund).setOnClickListener {
            Toast.makeText(context, "退款/售后", Toast.LENGTH_SHORT).show()
        }

        // 钱包相关点击
        view.findViewById<ImageView>(R.id.ivYoCoin).setOnClickListener {
            Toast.makeText(context, "YO 币", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivCoupon).setOnClickListener {
            Toast.makeText(context, "优惠券", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivReward).setOnClickListener {
            Toast.makeText(context, "打赏", Toast.LENGTH_SHORT).show()
        }

        // 工具相关点击
        view.findViewById<ImageView>(R.id.ivContactService).setOnClickListener {
            Toast.makeText(context, "联系客服", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivRealNameAuth).setOnClickListener {
            Toast.makeText(context, "实名认证", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivTalentAuth).setOnClickListener {
            Toast.makeText(context, "才艺认证", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivAddress).setOnClickListener {
            Toast.makeText(context, "收货地址", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivCreationCenter).setOnClickListener {
            Toast.makeText(context, "创作中心", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivBrowsingHistory).setOnClickListener {
            Toast.makeText(context, "浏览足迹", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivRuleChannel).setOnClickListener {
            Toast.makeText(context, "规则频道", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivChangeInviter).setOnClickListener {
            Toast.makeText(context, "更换邀请人", Toast.LENGTH_SHORT).show()
        }

        // 顶部图标点击
        view.findViewById<ImageView>(R.id.ivCalendar).setOnClickListener {
            Toast.makeText(context, "日历", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivCustomerService).setOnClickListener {
            Toast.makeText(context, "客服", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.ivSettings).setOnClickListener {
            Toast.makeText(context, "设置", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 跳转到订单页面
     * @param orderType 订单类型：0-全部，1-待付款，2-待发货，3-待收货，4-待评价
     */
    private fun navigateToOrders(view: View, orderType: Int) {
        val intent = Intent(requireContext(), MyOrdersActivity::class.java)
        intent.putExtra("ORDER_TYPE", orderType)
        startActivity(intent)
    }
}
package com.example.android_youbo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoodAdapter : RecyclerView.Adapter<GoodAdapter.GoodViewHolder>() {
    private val items = listOf(
        GoodItem("商品1", "¥99.00", "热销"),
        GoodItem("商品2", "¥199.00", "新品"),
        GoodItem("商品3", "¥299.00", "限时折扣"),
        GoodItem("商品4", "¥399.00", "爆款"),
        GoodItem("商品5", "¥499.00", "推荐")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_good, parent, false)
        return GoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    class GoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_good_title)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_good_price)
        private val tagTextView: TextView = itemView.findViewById(R.id.tv_good_sales)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_good_cover)

        fun bind(item: GoodItem) {
            titleTextView.text = item.title
            priceTextView.text = item.price
            tagTextView.text = item.tag
            // 这里可以设置图片资源
        }
    }

    data class GoodItem(
        val title: String,
        val price: String,
        val tag: String
    )
}
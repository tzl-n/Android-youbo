package com.example.android_youbo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 万能适配器 - 支持多种类型的RecyclerView适配器
 */
class UniversalAdapter<T>(
    private val layoutResId: Int,
    private val onBind: (holder: ViewHolder, item: T, position: Int) -> Unit
) : RecyclerView.Adapter<UniversalAdapter.ViewHolder>() {

    private var dataList: MutableList<T> = mutableListOf()
    private var onItemClick: ((item: T, position: Int) -> Unit)? = null
    private var onItemLongClick: ((item: T, position: Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        val holder = ViewHolder(view)
        
        // 设置点击事件
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(dataList[position], position)
            }
        }
        
        // 设置长按事件
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemLongClick?.invoke(dataList[position], position) ?: false
            } else {
                false
            }
        }
        
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < dataList.size) {
            onBind.invoke(holder, dataList[position], position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    /**
     * 更新数据
     */
    fun updateData(newData: List<T>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     */
    fun addData(newData: List<T>) {
        val startPosition = dataList.size
        dataList.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    /**
     * 插入单个数据
     */
    fun insertData(position: Int, item: T) {
        dataList.add(position, item)
        notifyItemInserted(position)
    }

    /**
     * 删除数据
     */
    fun removeData(position: Int) {
        if (position >= 0 && position < dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 获取指定位置的数据
     */
    fun getItem(position: Int): T? {
        return if (position >= 0 && position < dataList.size) {
            dataList[position]
        } else {
            null
        }
    }

    /**
     * 获取所有数据
     */
    fun getData(): List<T> = dataList.toList()

    /**
     * 清空数据
     */
    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    /**
     * 设置点击监听
     */
    fun setOnItemClickListener(listener: (item: T, position: Int) -> Unit) {
        onItemClick = listener
    }

    /**
     * 设置长按监听
     */
    fun setOnItemLongClickListener(listener: (item: T, position: Int) -> Boolean) {
        onItemLongClick = listener
    }

    /**
     * ViewHolder类
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun <T : View> getView(viewId: Int): T {
            return itemView.findViewById(viewId)
        }
    }
}
package com.example.android_youbo.model

/**
 * 商品数据模型
 */
data class GoodModel(
    val id: Long,
    val title: String,
    val coverUrl: String,
    val price: String,
    val salesCount: Int,
    val detailUrl: String = ""
)

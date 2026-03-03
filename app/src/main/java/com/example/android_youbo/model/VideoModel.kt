package com.example.android_youbo.model

/**
 * 视频数据模型
 */
data class VideoModel(
    val id: Long,
    val title: String,
    val coverUrl: String,
    val videoUrl: String,
    val duration: String,
    val playCount: Long,
    val likeCount: Long,
    val commentCount: Long,
    val author: Author,
    val createTime: String
)

/**
 * 视频作者信息
 */
data class Author(
    val id: Long,
    val nickname: String,
    val avatar: String,
    val fansCount: Long
)

/**
 * 视频列表响应数据
 */
data class VideoListResponse(
    val code: Int,
    val message: String,
    val data: List<VideoModel>
)
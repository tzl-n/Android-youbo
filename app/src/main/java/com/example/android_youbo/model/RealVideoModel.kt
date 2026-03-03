package com.example.android_youbo.model

/**
 * 实际API返回的视频数据模型
 */
data class RealVideoModel(
    val id: Long,
    val name: String,  // 作者名称
    val title: String, // 视频标题
    val description: String,
    val ctime: String, // 创建时间
    val videomainimag: String, // 视频封面图片
    val image_url: String, // 视频图片URL
    val playnum: Int, // 播放数
    val commentnum: Int, // 评论数
    val userid: String, // 用户ID
    val avatar_url: String, // 用户头像
    val videopath: String, // 视频路径
    val preview_url: String, // 预览URL
    val labelIds: String, // 标签ID
    val verifycode: String,
    val channelid: String,
    val group_id: String,
    val item_id: String,
    val publish_time: String?
)

/**
 * API响应包装类
 */
data class VideoApiResponse(
    val code: Int,
    val msg: String,
    val data: List<RealVideoModel>
)

/**
 * 转换为统一的视频模型用于UI显示
 */
fun RealVideoModel.toDisplayModel(): VideoModel {
    return VideoModel(
        id = this.id,
        title = this.title.ifEmpty { this.description },
        coverUrl = this.videomainimag.ifEmpty { this.image_url },
        videoUrl = this.videopath,
        duration = "00:00", // API 未返回具体时长
        playCount = this.playnum.toLong(),
        likeCount = 0L, // API 未返回点赞数
        commentCount = this.commentnum.toLong(),
        author = Author(
            id = this.userid.toLongOrNull() ?: 0L,
            nickname = this.name,
            avatar = this.avatar_url,
            fansCount = 0L // API 未返回粉丝数
        ),
        createTime = this.ctime
    )
}
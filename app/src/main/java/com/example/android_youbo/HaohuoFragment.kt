package com.example.android_youbo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.android_youbo.adapter.UniversalAdapter
import com.example.android_youbo.model.Author
import com.example.android_youbo.model.RealVideoModel
import com.example.android_youbo.model.VideoModel
import com.example.android_youbo.model.toDisplayModel
import com.example.android_youbo.network.ApiClient
import com.example.android_youbo.network.ApiService
import kotlinx.coroutines.launch

class HaohuoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var btnSearch: Button
    
    // 视频列表
    private lateinit var videoAdapter: UniversalAdapter<VideoModel>
    private var realVideoList: List<RealVideoModel> = emptyList()
    private val apiService = ApiClient.createService<ApiService>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_haohuo, container, false)
        
        // 初始化组件
        recyclerView = view.findViewById(R.id.recycler_view)
        searchInput = view.findViewById(R.id.search_input)
        btnSearch = view.findViewById(R.id.btn_search)
        
        // 初始化视频瀑布流
        setupVideoRecyclerView()
        
        // 加载视频数据
        loadVideoData()
        
        // 初始化搜索功能
        setupSearchFeatures()
        
        return view
    }
    
    private fun setupVideoRecyclerView() {
        // 创建瀑布流布局管理器
        val staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredLayoutManager
            
        // 创建万能适配器
        videoAdapter = UniversalAdapter(
            R.layout.item_video
        ) { holder, item, position ->
            // 绑定视频视图
            bindVideoItem(holder, item, position)
        }
            
        // 设置点击事件
        videoAdapter.setOnItemClickListener { item, position ->
            Toast.makeText(context, "点击了视频：${item.title}", Toast.LENGTH_SHORT).show()
            // 这里可以跳转到对应的详情页
        }
            
        recyclerView.adapter = videoAdapter
    }
    
    private fun bindVideoItem(holder: UniversalAdapter.ViewHolder, item: VideoModel, position: Int) {
        // 绑定视频封面
        val coverImage = holder.getView<ImageView>(R.id.iv_video_cover)
        
        // 重置高度为默认值（避免复用导致的高度混乱）
        val layoutParams = coverImage.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        coverImage.layoutParams = layoutParams
        
        // 设置图片加载监听器，根据图片实际尺寸调整布局
        if (item.coverUrl.isNotEmpty()) {
            Glide.with(this)
                .load(item.coverUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    override fun onResourceReady(resource: android.graphics.drawable.Drawable, transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?) {
                        // 获取图片实际尺寸
                        val drawableWidth = resource.intrinsicWidth
                        val drawableHeight = resource.intrinsicHeight
                        
                        // 计算宽高比
                        val aspectRatio = if (drawableWidth > 0) {
                            drawableHeight.toFloat() / drawableWidth.toFloat()
                        } else {
                            0.5625f // 默认 16:9
                        }
                        
                        // 根据宽度和宽高比计算高度
                        val imageViewWidth = coverImage.width
                        if (imageViewWidth > 0) {
                            val calculatedHeight = (imageViewWidth * aspectRatio).toInt()
                            val layoutParams = coverImage.layoutParams
                            layoutParams.height = calculatedHeight
                            coverImage.layoutParams = layoutParams
                        } else {
                            // 如果宽度还未测量，使用默认比例
                            val defaultHeight = (coverImage.measuredWidth * aspectRatio).toInt()
                            if (defaultHeight > 0) {
                                val layoutParams = coverImage.layoutParams
                                layoutParams.height = defaultHeight
                                coverImage.layoutParams = layoutParams
                            }
                        }
                        
                        coverImage.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                        coverImage.setImageDrawable(placeholder)
                    }
                })
        } else {
            coverImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        // 绑定时长
        val durationText = holder.getView<TextView>(R.id.tv_duration)
        durationText.text = item.duration
        
        // 绑定标题
        val titleText = holder.getView<TextView>(R.id.tv_video_title)
        titleText.text = item.title
        
        // 绑定作者头像
        val avatarImage = holder.getView<ImageView>(R.id.civ_author_avatar)
        if (item.author.avatar.isNotEmpty()) {
            Glide.with(this)
                .load(item.author.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(avatarImage)
        } else {
            avatarImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        // 绑定作者名称
        val authorName = holder.getView<TextView>(R.id.tv_author_name)
        authorName.text = item.author.nickname
    }
    
    private fun loadVideoData() {
        lifecycleScope.launch {
            try {
                val response = apiService.getRecommendVideos(3, 20)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.code == 200) {
                        realVideoList = apiResponse.data
                        val displayVideos = realVideoList.map { it.toDisplayModel() }
                        videoAdapter.updateData(displayVideos)
                        Toast.makeText(context, "加载了${displayVideos.size}个视频", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "API 返回错误：${apiResponse?.msg}", Toast.LENGTH_SHORT).show()
                        loadMockVideoData()
                    }
                } else {
                    Toast.makeText(context, "加载视频失败：${response.code()}", Toast.LENGTH_SHORT).show()
                    loadMockVideoData()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "网络错误：${e.message}", Toast.LENGTH_SHORT).show()
                loadMockVideoData()
            }
        }
    }
    
    private fun loadMockVideoData() {
        val mockVideos = listOf(
            VideoModel(
                id = 1,
                title = "美食制作教程 - 家常菜做法",
                coverUrl = "",
                videoUrl = "",
                duration = "02:30",
                playCount = 125000,
                likeCount = 8300,
                commentCount = 1200,
                author = Author(
                    id = 1,
                    nickname = "美食达人",
                    avatar = "",
                    fansCount = 120000
                ),
                createTime = "2024-01-01"
            ),
            VideoModel(
                id = 2,
                title = "旅行日记 - 海边度假胜地",
                coverUrl = "",
                videoUrl = "",
                duration = "03:45",
                playCount = 89000,
                likeCount = 5600,
                commentCount = 890,
                author = Author(
                    id = 2,
                    nickname = "旅行博主",
                    avatar = "",
                    fansCount = 89000
                ),
                createTime = "2024-01-02"
            ),
            VideoModel(
                id = 3,
                title = "健身训练 - 居家锻炼指南",
                coverUrl = "",
                videoUrl = "",
                duration = "05:20",
                playCount = 156000,
                likeCount = 12500,
                commentCount = 2100,
                author = Author(
                    id = 3,
                    nickname = "健身教练",
                    avatar = "",
                    fansCount = 234000
                ),
                createTime = "2024-01-03"
            )
        )
        videoAdapter.updateData(mockVideos)
    }
    
    private fun formatCount(count: Long): String {
        return when {
            count >= 10000 -> "${count / 10000}w"
            count >= 1000 -> "${count / 1000}k"
            else -> count.toString()
        }
    }

    private fun setupSearchFeatures() {
        // 设置搜索按钮点击事件
        btnSearch.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(context, "请输入搜索内容", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 设置搜索输入框的回车键监听
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
    }
    
    private fun performSearch(query: String) {
        // TODO: 实现实际的搜索逻辑
        Toast.makeText(context, "搜索：$query", Toast.LENGTH_SHORT).show()
        // 可以调用 API 或本地搜索功能
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = HaohuoFragment()
    }
}

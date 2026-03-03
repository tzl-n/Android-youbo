package com.example.android_youbo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android_youbo.adapter.UniversalAdapter
import com.example.android_youbo.model.RealVideoModel
import com.example.android_youbo.model.VideoModel
import com.example.android_youbo.model.toDisplayModel
import com.example.android_youbo.network.ApiClient
import com.example.android_youbo.network.ApiService
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.view.inputmethod.EditorInfo
import com.bumptech.glide.Glide
import com.example.android_youbo.model.Author
import kotlinx.coroutines.launch


class YouboFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var voiceSearchButton: ImageButton
    private lateinit var scanButton: ImageButton
    private var speechRecognizer: SpeechRecognizer? = null
    
    // 只使用 VideoModel 作为数据类型
    private lateinit var videoAdapter: UniversalAdapter<VideoModel>
    private var realVideoList: List<RealVideoModel> = emptyList()
    private val apiService = ApiClient.createService<ApiService>()
    
    companion object {
        private const val PERMISSION_REQUEST_RECORD_AUDIO = 1
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_youbo, container, false)
        
        // 初始化组件
        tabLayout = view.findViewById(R.id.tab_layout)
        recyclerView = view.findViewById(R.id.recycler_view)
        searchInput = view.findViewById(R.id.search_input)
        voiceSearchButton = view.findViewById(R.id.btn_voice_search_top)
        scanButton = view.findViewById(R.id.btn_scan)
        
        // 设置顶部导航标签
        setupTabLayout()
        
        // 初始化视频瀑布流
        setupVideoRecyclerView()
        
        // 加载视频数据
        loadVideoData()
        
        // 初始化搜索功能
        setupSearchFeatures()
        
        return view
    }
    
    private fun setupTabLayout() {
        val tabs = listOf("精选", "有播", "直播", "短视频")
        tabs.forEach { tab ->
            tabLayout.newTab().setText(tab).also { tabLayout.addTab(it) }
        }
        
        // 设置默认选中第一个标签
        tabLayout.getTabAt(0)?.select()
    }
    
    private fun setupVideoRecyclerView() {
        // 创建瀑布流布局管理器
        val staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredLayoutManager
            
        // 创建万能适配器，只处理视频数据
        videoAdapter = UniversalAdapter(
            R.layout.item_video // 默认布局
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
                val response = apiService.getRecommendVideos(1, 20)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.code == 200) {
                        realVideoList = apiResponse.data
                        val displayVideos = realVideoList.map { it.toDisplayModel() }
                        videoAdapter.updateData(displayVideos)
                        Toast.makeText(context, "加载了${displayVideos.size}个视频", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "API返回错误: ${apiResponse?.msg}", Toast.LENGTH_SHORT).show()
                        loadMockVideoData()
                    }
                } else {
                    Toast.makeText(context, "加载视频失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                    loadMockVideoData()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "网络错误: ${e.message}", Toast.LENGTH_SHORT).show()
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
        // 设置搜索输入框的回车键监听
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
        
        // 初始化语音搜索
        setupVoiceSearch()
        
        // 初始化扫码功能
        setupScanFeature()
    }
    
    private fun setupVoiceSearch() {
        // 检查设备是否支持语音识别
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            voiceSearchButton.visibility = View.GONE
            return
        }
        
        // 初始化语音识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        
        // 设置点击事件
        voiceSearchButton.setOnClickListener {
            requestAudioPermission()
        }
        
        // 设置语音识别监听器
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> Toast.makeText(context, "音频录制错误", Toast.LENGTH_SHORT).show()
                    SpeechRecognizer.ERROR_NO_MATCH -> Toast.makeText(context, "未识别到语音", Toast.LENGTH_SHORT).show()
                    SpeechRecognizer.ERROR_NETWORK -> Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(context, "语音识别错误: $error", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    searchInput.setText(recognizedText)
                    // 自动执行搜索
                    performSearch(recognizedText)
                }
            }
            
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }
    
    private fun setupScanFeature() {
        scanButton.setOnClickListener {
            // 这里可以集成二维码扫描功能
            Toast.makeText(context, "扫码功能待实现", Toast.LENGTH_SHORT).show()
            // 可以使用 ZXing 或 ML Kit Barcode Scanning
        }
    }
    
    private fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_RECORD_AUDIO
            )
        } else {
            startVoiceRecognition()
        }
    }
    
    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "请说出您要搜索的内容")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        
        speechRecognizer?.startListening(intent)
    }
    
    private fun performSearch(query: String) {
        // 在这里实现实际的搜索逻辑
        Toast.makeText(context, "搜索: $query", Toast.LENGTH_SHORT).show()
        // 可以调用API或本地搜索功能
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVoiceRecognition()
                } else {
                    Toast.makeText(context, "需要录音权限才能使用语音搜索", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }
}
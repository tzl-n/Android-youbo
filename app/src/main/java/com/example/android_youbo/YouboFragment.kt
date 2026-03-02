package com.example.android_youbo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent


class YouboFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var voiceSearchButton: ImageButton
    private lateinit var scanButton: ImageButton
    private var speechRecognizer: SpeechRecognizer? = null
    
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
        
        // 初始化商品列表
        setupRecyclerView()
        
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
    
    private fun setupRecyclerView() {
        // 这里可以添加商品数据适配器
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = GoodAdapter()
    }
    
    private fun setupSearchFeatures() {
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
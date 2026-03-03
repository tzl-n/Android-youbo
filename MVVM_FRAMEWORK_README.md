# MVVM 网络请求框架使用说明

## 项目结构

```
com.example.android_youbo
├── base/                   # 基础类
│   ├── BaseActivity.kt     # Activity 基类
│   ├── BaseFragment.kt     # Fragment 基类
│   └── ViewModelFactory.kt # ViewModel 工厂
├── network/               # 网络层
│   ├── ApiClient.kt       # Retrofit 客户端
│   ├── ApiService.kt      # API 接口定义
│   ├── ApiResponse.kt     # 响应数据模型
│   ├── NetworkResult.kt   # 网络结果封装
│   ├── NetworkStatus.kt   # 网络状态枚举
│   └── NetworkExtensions.kt # 网络扩展函数
├── repository/            # 数据仓库层
│   ├── BaseRepository.kt  # Repository 基类
│   ├── ProductRepository.kt # 商品 Repository
│   └── UserRepository.kt   # 用户 Repository
├── viewmodel/             # 视图模型层
│   ├── BaseViewModel.kt   # ViewModel 基类
│   ├── ProductViewModel.kt # 商品 ViewModel
│   └── UserViewModel.kt    # 用户 ViewModel
├── model/                 # 数据模型层
│   ├── VideoModel.kt      # 视频显示模型
│   ├── RealVideoModel.kt  # API 原始视频模型
│   ├── GoodModel.kt       # 商品模型
│   └── Author.kt          # 作者模型
├── adapter/               # 适配器层
│   └── UniversalAdapter.kt # 万能适配器
├── ui/                    # UI 层示例
│   ├── ProductListActivity.kt  # 商品列表 Activity 示例
│   ├── ProductListFragment.kt  # 商品列表 Fragment 示例
│   └── YouboFragment.kt        # 有播视频瀑布流 Fragment（当前主要功能）
└── utils/                 # 工具类
    └── ApiTestUtil.kt     # API 测试工具
```

## 核心组件说明

### 1. 网络层 (Network Layer)
- **ApiClient**: Retrofit配置和客户端管理
- **ApiService**: API接口定义
- **ApiResponse**: 统一的响应数据格式
- **NetworkResult**: 网络请求结果封装
- **NetworkExtensions**: 网络请求扩展函数

### 2. 数据仓库层 (Repository Layer)
- **BaseRepository**: Repository基类，提供通用方法
- **ProductRepository**: 商品相关数据操作
- **UserRepository**: 用户相关数据操作

### 3. 视图模型层 (ViewModel Layer)
- **BaseViewModel**: ViewModel基类，提供协程支持和错误处理
- **ProductViewModel**: 商品相关业务逻辑
- **UserViewModel**: 用户相关业务逻辑

### 4. 基础类 (Base Classes)
- **BaseActivity**: Activity基类，集成ViewBinding和ViewModel
- **BaseFragment**: Fragment基类，集成ViewBinding和ViewModel
- **ViewModelFactory**: ViewModel工厂类

## 使用方法

### 1. 创建新的API接口
在 `ApiService.kt` 中添加新的接口方法：

``kotlin
@GET("users/{id}")
suspend fun getUser(@Path("id") userId: Long): Response<ApiResponse<User>>
```

### 2. 创建Repository
继承 `BaseRepository` 并实现业务逻辑：

``kotlin
class UserRepository : BaseRepository() {
    private val _userData = MutableLiveData<NetworkResult<User>>()
    val userData: LiveData<NetworkResult<User>> = _userData
    
    suspend fun getUser(userId: Long) {
        _userData.value = NetworkResult.Loading()
        
        val result = safeApiRequest {
            apiService.getUser(userId)
        }
        
        handleNetworkResult(result, _userData)
    }
}
```

### 3. 创建ViewModel
继承 `BaseViewModel`：

``kotlin
class UserViewModel : BaseViewModel() {
    private val repository = UserRepository()
    val userData: LiveData<NetworkResult<User>> = repository.userData
    
    fun loadUser(userId: Long) {
        safeLaunch {
            repository.getUser(userId)
        }
    }
}
```

### 4. 在Activity中使用
``kotlin
class UserActivity : BaseActivity<ActivityUserBinding, UserViewModel>() {
    
    override fun getViewBinding(): ActivityUserBinding {
        return ActivityUserBinding.inflate(layoutInflater)
    }
    
    override fun getViewModel(): UserViewModel {
        return ViewModelProvider(this, ViewModelFactory { UserViewModel() })[UserViewModel::class.java]
    }
    
    override fun initData() {
        viewModel.userData.observe(this) { result ->
            when (result) {
                is NetworkResult.Loading -> showLoading()
                is NetworkResult.Success -> {
                    hideLoading()
                    updateUserUI(result.data)
                }
                is NetworkResult.Error -> {
                    hideLoading()
                    handleError(result.message)
                }
            }
        }
        
        viewModel.loadUser(123)
    }
}
```

### 5. 在Fragment中使用
``kotlin
class UserFragment : BaseFragment<FragmentUserBinding, UserViewModel>() {
    
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }
    
    override fun getViewModel(): UserViewModel {
        return ViewModelProvider(this, ViewModelFactory { UserViewModel() })[UserViewModel::class.java]
    }
    
    override fun initData() {
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            // 处理结果
        }
    }
}
```

## 特性

1. **统一的错误处理**: 通过 `NetworkResult` 统一封装网络请求结果
2. **协程支持**: 使用 Kotlin 协程进行异步操作
3. **生命周期感知**: ViewModel 自动管理生命周期
4. **类型安全**: 使用泛型确保类型安全
5. **可扩展性**: 基类设计便于扩展新功能
6. **ViewBinding 支持**: 自动集成 ViewBinding 减少 findViewById 调用
7. **视频瀑布流**: 等比例封面图片展示，动态计算布局
8. **抖音风格 UI**: 信息叠加、渐变遮罩、圆形头像

## 当前项目功能

### 视频瀑布流展示
- **实现位置**: `YouboFragment.kt`
- **布局文件**: `item_video.xml`
- **API 接口**: `videosimple/getRecommendSimpleVideo`
- **核心特性**:
  - 根据图片原始比例动态计算高度
  - 信息叠加在封面底部（渐变遮罩）
  - 圆形头像显示作者信息
  - 白色文字配阴影增强对比度
  - 移除播放按钮，简洁设计

### 网络请求配置
- **Base URL**: `http://10.161.9.80:7015/`
- **认证方式**: Token (JWT)
- **请求头**: 
  - `token`: JWT 令牌
  - `Accept`: `*/*`
  - `Content-Type`: `application/x-www-form-urlencoded`

### 数据模型映射
```
RealVideoModel (API 原始数据)
  ↓ toDisplayModel()
VideoModel (UI 显示数据)
  ↓
UniversalAdapter (万能适配器)
  ↓
RecyclerView (瀑布流展示)
```

## 注意事项

1. 记得在 `ApiClient.kt` 中替换真实的 BASE_URL
2. 根据实际 API 响应格式调整 `ApiResponse` 类
3. 根据项目需要添加 token 认证等安全机制（已实现）
4. 建议添加网络缓存机制提升用户体验
5. 可以根据需要添加更多的网络拦截器
6. **视频封面**: 优先使用 `videomainimag` 字段，备用 `image_url`
7. **等比例布局**: 使用 Glide CustomTarget 监听图片加载
8. **RecyclerView 复用**: 重置高度避免复用导致的高度混乱

## 快速开始示例（视频瀑布流）

### 1. 定义 API 接口
``kotlin
@GET("videosimple/getRecommendSimpleVideo")
suspend fun getRecommendVideos(
    @Query("page") page: Int = 1,
    @Query("pagesize") pageSize: Int = 20
): Response<VideoApiResponse>
```

### 2. 数据模型转换
``kotlin
fun RealVideoModel.toDisplayModel(): VideoModel {
    return VideoModel(
        id = this.id,
        title = this.title.ifEmpty { this.description },
        coverUrl = this.videomainimag.ifEmpty { this.image_url },
        videoUrl = this.videopath,
        duration = "00:00",
        author = Author(
            id = this.userid.toLongOrNull() ?: 0L,
            nickname = this.name,
            avatar = this.avatar_url,
            fansCount = 0L
        ),
        createTime = this.ctime
    )
}
```

### 3. 在 Fragment 中使用
``kotlin
class YouboFragment : Fragment() {
    private lateinit var videoAdapter: UniversalAdapter<VideoModel>
    
    private fun setupVideoRecyclerView() {
        val staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredLayoutManager
        
        videoAdapter = UniversalAdapter(R.layout.item_video) { holder, item, position ->
            bindVideoItem(holder, item, position)
        }
        
        recyclerView.adapter = videoAdapter
    }
    
    private fun loadVideoData() {
        lifecycleScope.launch {
            val response = apiService.getRecommendVideos(1, 20)
            if (response.isSuccessful && response.body()?.code == 200) {
                val displayVideos = response.body()!!.data.map { it.toDisplayModel() }
                videoAdapter.updateData(displayVideos)
            }
        }
    }
}
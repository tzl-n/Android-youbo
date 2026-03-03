# MVVM网络请求框架概述

## 📋 框架简介

这是一个基于 **MVVM 架构模式** 的 Android 网络请求框架，采用现代化的技术栈，提供了完整的网络请求解决方案。框架遵循 Google 推荐的 Android 应用架构指南，实现了关注点分离和可测试性。

**当前项目重点功能**：视频瀑布流展示、等比例封面布局、抖音风格 UI 设计。

## 🏗️ 架构层次

### 1. 表示层 (Presentation Layer)
- **View**: Activity/Fragment 负责 UI 展示
- **ViewModel**: 处理 UI 相关数据和业务逻辑
- **LiveData**: 数据观察和状态管理
- **当前实现**: YouboFragment 视频瀑布流页面

### 2. 数据层 (Data Layer)  
- **Repository**: 数据源管理，统一数据访问接口
- **Remote Data Source**: 网络数据获取
- **Local Data Source**: 本地数据存储(可扩展)

### 3. 网络层 (Network Layer)
- **Retrofit**: HTTP客户端
- **OkHttp**: 网络拦截和日志
- **Gson**: JSON序列化/反序列化

## 📁 项目结构详解

### base/ - 基础组件层
```
BaseActivity.kt      # Activity基类，提供通用功能封装
BaseFragment.kt      # Fragment基类，提供通用功能封装  
ViewModelFactory.kt  # ViewModel工厂，简化实例创建
```

### network/ - 网络通信层
```
ApiClient.kt         # Retrofit客户端配置和初始化
ApiService.kt        # RESTful API接口定义
ApiResponse.kt       # 统一的服务器响应数据模型
NetworkResult.kt     # 网络请求结果密封类封装
NetworkStatus.kt     # 网络状态枚举定义
NetworkExtensions.kt # 网络请求扩展函数和工具方法
```

### repository/ - 数据仓库层
```
BaseRepository.kt    # Repository基类，提供通用数据操作方法
ProductRepository.kt # 商品相关数据操作实现
UserRepository.kt    # 用户相关数据操作实现
```

### viewmodel/ - 视图模型层
```
BaseViewModel.kt     # ViewModel基类，提供协程支持和错误处理
ProductViewModel.kt  # 商品相关业务逻辑处理
UserViewModel.kt     # 用户相关业务逻辑处理
```

## 🔧 核心特性

### ✅ 统一的错误处理
- 通过 `NetworkResult` 密封类统一封装所有网络请求结果
- 自动处理加载、成功、错误、空数据等状态
- 完善的异常捕获和错误信息传递

### ✅ 协程支持
- 使用 Kotlin 协程进行异步网络请求
- 统一的协程异常处理器
- 安全的协程作用域管理

### ✅ 生命周期感知
- ViewModel 自动管理生命周期
- 避免内存泄漏
- 自动取消未完成的网络请求

### ✅ 类型安全
- 使用泛型确保编译时类型安全
- 密封类提供完整的状态覆盖
- 编译期检查减少运行时错误

### ✅ 可扩展性
- 基类设计便于业务扩展
- 模块化架构易于维护
- 支持多种数据源整合

## 🚀 快速开始

### 1. 创建Repository
```kotlin
class MyRepository : BaseRepository() {
    suspend fun fetchData(): NetworkResult<MyData> {
        return safeApiRequest { apiService.getData() }
    }
}
```

### 2. 创建ViewModel
```kotlin
class MyViewModel : BaseViewModel() {
    private val repository = MyRepository()
    
    fun loadData() {
        safeLaunch {
            val result = repository.fetchData()
            // 处理结果
        }
    }
}
```

### 3. 在Activity中使用
```kotlin
class MyActivity : BaseActivity<MyViewModel>() {
    override fun createViewModel(): MyViewModel {
        return ViewModelProvider(this, ViewModelFactory { MyViewModel() })[MyViewModel::class.java]
    }
    
    override fun initData() {
        mViewModel.loadData()
    }
}
```

## 🛠️ 技术栈

- **语言**: Kotlin
- **架构**: MVVM + Repository Pattern
- **网络**: Retrofit2 + OkHttp3
- **异步**: Kotlin Coroutines
- **JSON**: Gson
- **生命周期**: Android Architecture Components
- **数据绑定**: ViewBinding (可选)

## 📊 数据流向

```
View(UI) → ViewModel → Repository → Remote API
   ↑           ↓            ↓            ↓
   ← LiveData更新 ← NetworkResult ← HTTP Response
```

## 🔒 最佳实践

1. **单一职责原则**: 每个组件只负责特定的功能
2. **依赖倒置**: 通过接口抽象依赖关系
3. **开闭原则**: 对扩展开放，对修改封闭
4. **状态管理**: 使用LiveData进行响应式数据更新
5. **错误处理**: 统一的错误处理机制

## 📈 性能优化

- 网络请求缓存策略
- 图片加载优化
- 数据库本地存储
- 内存泄漏防护
- 网络状态监听

## 🎯 适用场景

- 需要频繁网络请求的应用
- 数据驱动型 UI 应用
- 需要良好架构扩展性的项目
- 团队协作开发的大型项目
- **当前项目**: 短视频推荐应用、视频瀑布流展示

---
*该框架旨在提供一个标准化、可复用的 Android 开发解决方案*

## 📝 当前项目实现

### 视频瀑布流功能
- **位置**: `YouboFragment.kt`
- **布局**: `item_video.xml`
- **API**: `videosimple/getRecommendSimpleVideo`
- **特性**:
  - 等比例封面图片展示
  - 信息叠加在封面底部
  - 渐变遮罩效果
  - 圆形头像显示
  - 白色文字配阴影
  - 移除播放按钮，简洁设计

### 核心技术实现
1. **等比例瀑布流**: 根据图片原始宽高比动态计算高度
2. **Glide 加载**: 使用 CustomTarget 监听图片加载完成
3. **RecyclerView 复用优化**: 重置高度避免复用混乱
4. **布局叠加**: 使用 RelativeLayout 实现信息叠加效果
5. **网络请求**: Retrofit + 协程 + Token 认证
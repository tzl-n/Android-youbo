# Android Youbo - 视频电商平台

## 📱 项目简介

Android Youbo 是一个基于 **MVVM 架构** 的 Android 视频电商应用，采用 Kotlin + Java 混合开发，集成了视频播放、商品展示、社交互动等功能。

---

## 🏗️ 技术架构

### 核心架构
- **架构模式**: MVVM (Model-View-ViewModel)
- **开发语言**: Kotlin + Java (混合开发)
- **SDK 版本**: minSdk 24, targetSdk 34

### 技术栈

| 类别 | 技术 | 说明 |
|------|------|------|
| **架构组件** | ViewModel + LiveData | 生命周期感知数据管理 |
| **网络框架** | Retrofit + OkHttp | RESTful API 客户端 |
| **异步处理** | Kotlin Coroutines | 协程异步编程 |
| **数据解析** | Gson | JSON 序列化/反序列化 |
| **图片加载** | Glide | 高效图片缓存 |
| **数据存储** | **MMKV** | 高性能键值存储 |
| **推送服务** | 极光推送 (JPush) | 消息推送 |
| **崩溃收集** | Bugly | 异常监控 |
| **UI 框架** | ViewBinding | 类型安全的视图绑定 |
| **基础库** | AndroidX | Jetpack 组件库 |

---

## 📦 功能模块

### 核心功能

#### 1. **视频模块** (主要功能)
- ✅ **YouboFragment** - 有播视频流
  - 视频瀑布流展示
  - 推荐视频 API: `videosimple/getRecommendSimpleVideo`
  - 语音搜索功能
  - 封面图自适应高度
  
- ✅ **HaohuoFragment** - 好物视频
  - 商品视频展示
  - 视频与商品结合

#### 2. **用户模块**
- ✅ **MineFragment** - 我的页面
  - 用户信息管理
  - 订单入口
  - 设置功能

- ✅ **SplashActivity** - 启动页
  - 隐私政策弹窗
  - 用户协议确认
  - 自动跳转主界面

#### 3. **消息模块**
- ✅ **MessageFragment** - 消息列表
- ✅ **MessageOderActivity** - 订单交易消息
- ✅ **MyMessageActivity** - 互动消息

#### 4. **订单模块**
- ✅ **MyOrdersActivity** - 我的订单
  - 订单列表展示
  - 订单状态管理

#### 5. **其他功能**
- ✅ **TeenModeActivity** - 青少年模式
  - 首次启动提示弹窗
  - 模式切换功能
- ✅ **MainActivity** - 主页面容器
  - 底部导航栏
  - Fragment 切换管理

---

## 📂 项目结构

```
app/src/main/java/com/example/android_youbo/
├── AppApplication.kt              # Application 入口
├── MainActivity.kt                # 主页面
├── SplashActivity.kt              # 启动页
├── TeenModeActivity.kt            # 青少年模式
├── MyOrdersActivity.kt            # 订单管理
├── HaohuoFragment.kt              # 好物视频 Fragment
├── YouboFragment.kt               # 有播视频 Fragment
├── MessageFragment.kt             # 消息 Fragment
├── MineFragment.kt                # 我的 Fragment
├── NullFragment.kt                # 空 Fragment
├── adapter/                       # 适配器
│   └── UniversalAdapter.kt        # 万能适配器
├── base/                          # 基类
│   ├── BaseActivity.kt            # Activity 基类
│   ├── BaseFragment.kt            # Fragment 基类
│   └── ViewModelFactory.kt        # ViewModel 工厂
├── message/                       # 消息模块
│   ├── MessageOderActivity.java   # 订单消息 Activity
│   └── MyMessageActivity.kt       # 互动消息 Activity
├── model/                         # 数据模型
│   ├── GoodModel.kt               # 商品模型
│   ├── RealVideoModel.kt          # 真实视频模型
│   ├── VideoModel.kt              # 视频模型
│   └── Author.kt                  # 作者模型
├── network/                       # 网络层
│   ├── ApiClient.kt               # Retrofit 客户端
│   ├── ApiService.kt              # API 接口定义
│   ├── ApiResponse.kt             # API 响应封装
│   ├── NetworkResult.kt           # 网络结果封装
│   ├── NetworkStatus.kt           # 网络状态
│   └── NetworkExtensions.kt       # 网络扩展函数
├── repository/                    # 数据仓库
│   ├── BaseRepository.kt          # 基础仓库
│   ├── ProductRepository.kt       # 商品仓库
│   └── UserRepository.kt          # 用户仓库
├── viewmodel/                     # ViewModel 层
│   ├── BaseViewModel.kt           # 基础 ViewModel
│   ├── ProductViewModel.kt        # 商品 ViewModel
│   └── UserViewModel.kt           # 用户 ViewModel
└── utils/                         # 工具类
    └── MMKVUtils.kt               # MMKV 存储工具
```

---

## 🔧 核心配置

### API 配置
- **API 地址**: `http://10.161.9.80:7015/`
- **Token**: JWT Token 认证
- **超时设置**: 30 秒（连接/读取/写入）
- **日志拦截器**: 已启用 (Level.BODY)

### 权限配置
```xml
<!-- 网络相关 -->
INTERNET
ACCESS_NETWORK_STATE
ACCESS_WIFI_STATE

<!-- 功能相关 -->
RECORD_AUDIO (语音搜索)
```

### 第三方服务
- **Bugly**: `b7ca37f2c4` (崩溃收集)
- **JPush**: `e8f59e14fd1473f75384a10b` (消息推送)
- **MMKV**: 1.3.9 (数据存储)

---

## 💾 数据存储方案

### MMKV 使用场景
✅ **青少年模式确认状态** (`teen_mode_acknowledged`)  
✅ **隐私政策同意状态** (`agreement_accepted`)  

### MMKV 优势对比

| 特性 | SharedPreferences | MMKV |
|------|------------------|------|
| **性能** | 较慢，全量读取 | ⚡ 极快，mmap 内存映射 |
| **线程安全** | 需要手动处理 | ✅ 原生支持 |
| **崩溃率** | ANR 风险较高 | 🛡️ 几乎无 ANR |
| **跨进程** | ❌ 不支持 | ✅ 支持 |
| **大小限制** | 有限制 | ∞ 无限制 |
| **加密** | ❌ 不支持 | ✅ AES 加密 |

---

## 🎯 开发规范

### 代码规范
- ✅ 使用 ViewBinding 进行视图绑定
- ✅ 使用 Coroutines 进行异步操作
- ✅ 遵循 MVVM 架构模式
- ✅ 使用 Repository 管理数据源
- ✅ 使用 Lifecycle 感知组件

### 网络请求规范
根据记忆中的开发规范：
1. **前置检查**: API 请求前必须检查网络状态
2. **权限验证**: 确保网络权限和动态权限已配置
3. **错误处理**: 无网络时提前回调失败信息

### 样式管理
- ✅ 使用 `themes.xml` 管理样式
- ❌ 不使用 `styles.xml`

### Lottie 动画使用
- ✅ 动画结束后需手动重置到第一帧
- ✅ 使用 `progress = 0f` 重置

---

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog 或更高版本
- JDK 11+
- Android SDK 34
- Gradle 8.9+

### 构建步骤
```bash
# 1. 克隆项目
git clone <repository-url>
cd Android-youbo

# 2. 同步 Gradle
./gradlew sync

# 3. 构建 Debug 版本
./gradlew assembleDebug

# 4. 安装到设备
./gradlew installDebug
```

### 分支说明
- `main` - 主分支（稳定版本）
- `lu` - 独立分支
- `jjx`, `yyh`, `lin`, `qsh` - 功能分支

---

## 📊 编译产物

### APK 输出路径
```
app/build/intermediates/apk/debug/app-debug.apk
```

### 版本信息
- **versionCode**: 1
- **versionName**: 1.0
- **applicationId**: com.example.android_youbo

---

## 🐛 已知问题

### IDE 相关
- ⚠️ IDE 可能显示 MMKV 导入错误（红色波浪线），但实际编译通过
- **解决方案**: 重启 Android Studio 或执行 Build → Rebuild Project

### NDK 相关
- ⚠️ 构建时会提示 NDK 缺少 source.properties 文件
- **影响**: 不影响应用正常运行

---

## 📝 更新日志

### v1.0.0 (最新)
- ✅ 完成 MVVM 架构搭建
- ✅ 实现视频瀑布流功能
- ✅ 集成 MMKV 替代 SharedPreferences
- ✅ 完善青少年模式和隐私政策弹窗
- ✅ 优化底部导航交互体验
- ✅ 集成 Bugly 崩溃收集
- ✅ 集成极光推送

---

## 📄 开源协议

本项目仅供学习参考

---

## 👥 开发团队

- 开发者：Android Youbo Team
- 项目地址：https://github.com/tzl-n/Android-youbo

---

## 📞 联系方式

如有问题请提交 Issue 或联系开发团队

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.android_youbo"
    compileSdk = 34
    
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.android_youbo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        manifestPlaceholders["JPUSH_PKGNAME"] = "com.example.android_youbo"
        manifestPlaceholders["JPUSH_APPKEY"] = "e8f59e14fd1473f75384a10b"
        manifestPlaceholders["JPUSH_CHANNEL"] = "developer-default"
    }
//    manifestPlaceholders.putAll(mapOf(
//        "JPUSH_PKGNAME" to applicationId,
//        "JPUSH_APPKEY" to "e8f59e14fd1473f75384a10b",
//        "JPUSH_CHANNEL" to "default_developer"
//    ))

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // 注释掉可能导致问题的 nativecrashreport 依赖
//     implementation (libs.nativecrashreport)
//    implementation (libs.crashreport)
    implementation ("com.tencent.bugly:crashreport:4.1.9.3")
//    implementation (libs.jpush)
    implementation ("cn.jiguang.sdk:jpush:5.5.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // MVVM Architecture Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // MMKV - 高性能键值存储
    implementation("com.tencent:mmkv-static:1.3.9")
    

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
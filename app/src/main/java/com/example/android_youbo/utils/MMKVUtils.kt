package com.example.android_youbo.utils

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * MMKV 工具类
 * 高性能键值存储，替代 SharedPreferences
 */
object MMKVUtils {
    
    private var mmkv: MMKV? = null
    
    /**
     * 初始化 MMKV
     * 需要在 Application onCreate 中调用
     */
    fun init(context: Context) {
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()
    }
    
    /**
     * 获取 MMKV 实例
     */
    fun getMMKV(): MMKV {
        return mmkv ?: throw IllegalStateException("MMKV not initialized. Call init() first.")
    }
    
    // ==================== Boolean 操作 ====================
    
    /**
     * 保存 Boolean 值
     */
    fun saveBoolean(key: String, value: Boolean) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 Boolean 值
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return getMMKV().decodeBool(key, defaultValue)
    }
    
    // ==================== String 操作 ====================
    
    /**
     * 保存 String 值
     */
    fun saveString(key: String, value: String?) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 String 值
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return getMMKV().decodeString(key, defaultValue)
    }
    
    // ==================== Int 操作 ====================
    
    /**
     * 保存 Int 值
     */
    fun saveInt(key: String, value: Int) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 Int 值
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return getMMKV().decodeInt(key, defaultValue)
    }
    
    // ==================== Long 操作 ====================
    
    /**
     * 保存 Long 值
     */
    fun saveLong(key: String, value: Long) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 Long 值
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return getMMKV().decodeLong(key, defaultValue)
    }
    
    // ==================== Float 操作 ====================
    
    /**
     * 保存 Float 值
     */
    fun saveFloat(key: String, value: Float) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 Float 值
     */
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return getMMKV().decodeFloat(key, defaultValue)
    }
    
    // ==================== Set 操作 ====================
    
    /**
     * 保存 Set<String> 值
     */
    fun saveStringSet(key: String, value: MutableSet<String>) {
        getMMKV().encode(key, value)
    }
    
    /**
     * 获取 Set<String> 值
     */
    fun getStringSet(key: String, defaultValue: MutableSet<String>? = null): MutableSet<String>? {
        return getMMKV().decodeStringSet(key, defaultValue)
    }
    
    // ==================== 其他操作 ====================
    
    /**
     * 检查是否包含某个 key
     */
    fun contains(key: String): Boolean {
        return getMMKV().containsKey(key)
    }
    
    /**
     * 删除指定 key
     */
    fun remove(key: String) {
        // 使用 encode 方法，传入 null 值来删除
        getMMKV().encode(key, null as String?)
    }
    
    /**
     * 清空所有数据
     */
    fun clearAll() {
        getMMKV().clearAll()
    }
    
    /**
     * 异步保存（推荐）
     * MMKV 默认是实时持久化的，不需要手动调用 apply
     */
    @Deprecated("MMKV is real-time persistent, no need to call apply manually")
    fun apply() {
        // MMKV 不需要手动 apply
    }
    
    /**
     * 同步保存（阻塞，慎用）
     * MMKV 默认是实时持久化的
     */
    @Deprecated("MMKV is real-time persistent, no need to call commit manually")
    fun commit(): Boolean {
        getMMKV().sync()
        return true
    }
}

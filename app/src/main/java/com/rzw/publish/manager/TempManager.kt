package com.rzw.publish.manager

/**
 * 缓存临时数据管理类
 */
class TempManager {

    private var imageList: MutableList<String> = mutableListOf()

    /**
     * 设置临时缓存
     */
    fun setTempImageList(it: List<String>) {
        imageList.clear()
        imageList.addAll(it)
    }

    /**
     * 获取临时数据
     */
    fun getTempImageList(): MutableList<String> {
        return imageList
    }

    /**
     * 清除临时缓存
     */
    fun resetTempImageList() {
        imageList.clear()
    }


    companion object {
        @Volatile
        private var instance: TempManager? = null

        @JvmStatic
        fun getInstance(): TempManager {
            return instance ?: synchronized(this) {
                instance ?: TempManager().also { instance = it }
            }
        }
    }
}
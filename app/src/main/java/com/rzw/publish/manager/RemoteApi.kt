package com.rzw.publish.manager

import android.net.Uri
import android.util.Base64
import androidx.annotation.WorkerThread
import com.blankj.utilcode.util.LogUtils
import com.rzw.publish.bean.RemoteImage

object RemoteApi {
    /**
     * 上传图片资源
     * @param localUri 本地图片路径
     * @return 服务器图片 uid
     * */
    @WorkerThread
    fun imageUpload(no:Int,localUri: Uri): RemoteImage {
        LogUtils.e("imageUpload-->","开始上传图片"+localUri.path)
        Thread.sleep(5000L)
        return RemoteImage(Base64.encodeToString(localUri.toString().toByteArray(), Base64.NO_WRAP),no)
    }

    /**
     * 发布图片
     * @param list 服务器图片 uid 列表
     * */
    @Throws
    @WorkerThread
    fun submit(list: List<String>) {
        Thread.sleep(2000L)
        if (list.isNullOrEmpty()) {
            throw NullPointerException()
        }
        Thread.sleep(2000L)
    }

}
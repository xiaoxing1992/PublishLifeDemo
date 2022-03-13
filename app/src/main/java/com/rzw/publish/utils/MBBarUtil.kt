package com.rzw.publish.utils

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils

object MBBarUtil {

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param statusBarColor
     * @param isLightMode
     */
    fun setStatusBarColor(activity: Activity, @ColorRes statusBarColor: Int, isLightMode: Boolean) {
        // 设置状态栏颜色
        BarUtils.setStatusBarColor(activity, ContextCompat.getColor(activity,statusBarColor))
        // 设置状态栏模式
        BarUtils.setStatusBarLightMode(activity, isLightMode)
    }
}
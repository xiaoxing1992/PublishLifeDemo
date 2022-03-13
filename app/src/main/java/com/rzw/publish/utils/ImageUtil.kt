package com.rzw.publish.utils

import com.luck.picture.lib.entity.LocalMedia

object ImageUtil {

    fun getImagePath(media: LocalMedia):String{
        return if (media.isCut && !media.isCompressed) {
            // 裁剪过
            media.cutPath
        } else if (media.isCut || media.isCompressed) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            media.compressPath
        } else {
            // 原图
            media.path
        }
    }

    fun getLocalPath(){

    }
}
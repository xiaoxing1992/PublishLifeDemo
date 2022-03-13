package com.rzw.publish.utils

import android.content.Context
import com.luck.picture.lib.basic.PictureSelectionSystemModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.MediaUtils
import java.util.ArrayList

object PicSelectUtils {

    fun publishImageSingle(context: Context,block:(LocalMedia)->Unit) {
        val systemGalleryMode: PictureSelectionSystemModel =
            PictureSelector.create(context)
                .openSystemGallery(SelectMimeType.TYPE_IMAGE)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCompressEngine(ImageCompressEngine())
                .setSkipCropMimeType(PictureMimeType.ofGIF(), PictureMimeType.ofWEBP())
                .isOriginalControl(true)
                .setSandboxFileEngine(MeSandboxFileEngine())
        systemGalleryMode.forSystemResultActivity(object :
            OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                handleResult(result,block)
            }

            override fun onCancel() {}
        })
    }

    //处理选择结果
    private fun handleResult(result: ArrayList<LocalMedia>,block:(LocalMedia)->Unit) {
        result.forEach { media ->
            if (media.width == 0 || media.height == 0) {
                if (PictureMimeType.isHasImage(media.mimeType)) {
                    val imageExtraInfo = MediaUtils.getImageSize(media.path)
                    media.width = imageExtraInfo.width
                    media.height = imageExtraInfo.height
                }
            }
        }
        block.invoke(result.first())
    }
}
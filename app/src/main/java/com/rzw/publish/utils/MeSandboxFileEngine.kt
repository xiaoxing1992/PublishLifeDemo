package com.rzw.publish.utils

import android.content.Context
import android.text.TextUtils
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.utils.SandboxTransformUtils

class MeSandboxFileEngine: SandboxFileEngine {
    override fun onStartSandboxFileTransform(
        context: Context?,
        isOriginalImage: Boolean,
        index: Int,
        media: LocalMedia,
        listener: OnCallbackIndexListener<LocalMedia>?
    ) {
        if (PictureMimeType.isContent(media.availablePath)) {
            val sandboxPath = SandboxTransformUtils.copyPathToSandbox(
                context, media.path,
                media.mimeType
            )
            media.sandboxPath = sandboxPath
        }
        if (isOriginalImage) {
            val originalPath = SandboxTransformUtils.copyPathToSandbox(
                context, media.path,
                media.mimeType
            )
            media.originalPath = originalPath
            media.isOriginal = !TextUtils.isEmpty(originalPath)
        }
        listener?.onCall(media, index)
    }
}
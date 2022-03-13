package com.rzw.publish.viewmodel

import androidx.lifecycle.MutableLiveData
import com.luck.picture.lib.entity.LocalMedia
import com.rzw.publish.base.BaseViewModel
import com.rzw.publish.manager.UploadManager

/**
 * 发布动态->图片
 */
class PublishNewsViewModel:BaseViewModel() {

    private var _internetPublishStateLiveData = MutableLiveData<List<String>>()
    val internetPublishStateLiveData get() = _internetPublishStateLiveData

    private var _dialogLiveData = MutableLiveData<Boolean>()
    val dialogLiveData get() = _dialogLiveData

    fun uploadSyncImage(data: MutableList<LocalMedia>) {
        _dialogLiveData.postValue(true)
        UploadManager.getInstance().uploadSyncImage(data,{
            _internetPublishStateLiveData.postValue(it)
            _dialogLiveData.postValue(false)
        },{
            //处理错误情况
            _internetPublishStateLiveData.postValue(null)
            _dialogLiveData.postValue(false)
        })
    }
}
package com.rzw.publish.manager

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Base64
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.entity.LocalMedia
import com.rzw.publish.bean.RemoteImage
import com.rzw.publish.utils.ImageUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.*

class UploadManager {

    private val UPLOAD_EXECUTOR: ExecutorService = ThreadPoolExecutor(
        3,
        3,
        (1000 * 3), TimeUnit.MILLISECONDS,
       // ArrayBlockingQueue(10),
        LinkedBlockingDeque(),
        Executors.defaultThreadFactory())


    @SuppressLint("CheckResult")
    fun uploadSyncImage(localList: MutableList<LocalMedia>, blockSuccess: (List<String>) -> Unit, blockError: (Throwable) -> Unit){

        Observable
            .merge(localList.mapIndexed { i, arg -> Observable.create<RemoteImage> {
                val result = UploadInternetTask(i, Uri.parse(ImageUtil.getImagePath(localList[i]))).call()
                it.onNext(result)
                it.onComplete()
            }    .subscribeOn(Schedulers.from(UPLOAD_EXECUTOR))})
            .toList()
            .flatMap { args -> Single.create<List<String>>
            {
                args.sort()
                val result = PublishImageTask(args).call()
                it.onSuccess(result)
            }.subscribeOn(Schedulers.newThread()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list -> blockSuccess.invoke(list) },
                { err -> blockError.invoke(err)}
            )
    }

    class UploadInternetTask(private val no: Int, private val uri: Uri) : Callable<RemoteImage> {
        override fun call(): RemoteImage {
            LogUtils.e("Task #$no is started on Thread #${Thread.currentThread().id}.")
            val remoteImage = RemoteApi.imageUpload(no,uri)
            LogUtils.e("Task #$no is done after")
            return remoteImage
        }
    }

    class PublishImageTask(private val args: List<RemoteImage>) : Callable<List<String>> {
        override fun call(): List<String> {
            val internetUrlList = mutableListOf<String>()
            args.forEach {
                internetUrlList.add(String(Base64.decode(it.uid,Base64.DEFAULT)))
            }
            LogUtils.e("Sum task is started on Thread #${Thread.currentThread().id} with args: ${args.joinToString()}.")
            RemoteApi.submit(internetUrlList)
            LogUtils.e("Sum task is done.")
            return internetUrlList
        }
    }

    companion object {
        @Volatile
        private var instance: UploadManager? = null

        @JvmStatic
        fun getInstance(): UploadManager {
            return instance ?: synchronized(this) {
                instance ?: UploadManager().also { instance = it }
            }
        }
    }
}
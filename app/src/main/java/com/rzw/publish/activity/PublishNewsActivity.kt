package com.rzw.publish.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v3.WaitDialog
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DensityUtil
import com.rzw.publish.BuildConfig
import com.rzw.publish.R
import com.rzw.publish.adapter.ImageGridAdapter
import com.rzw.publish.base.BaseActivity
import com.rzw.publish.databinding.ActivityPublishNewsBinding
import com.rzw.publish.manager.TempManager
import com.rzw.publish.utils.MBBarUtil
import com.rzw.publish.utils.PicSelectUtils
import com.rzw.publish.view.FullyGridLayoutManager
import com.rzw.publish.view.GridSpacingItemDecoration
import com.rzw.publish.viewmodel.PublishNewsViewModel

/**
 *
 * 发布图片
 */
class PublishNewsActivity : BaseActivity<PublishNewsViewModel, ActivityPublishNewsBinding>() {

    private val mLayoutManager: FullyGridLayoutManager by lazy {
        FullyGridLayoutManager(
            this,
            3,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    private val mPicList = mutableListOf<LocalMedia>()

    private val mAdapter: ImageGridAdapter by lazy { ImageGridAdapter(this) }

    override fun layoutId(): Int = R.layout.activity_publish_news

    override fun initView(savedInstanceState: Bundle?) {
        MBBarUtil.setStatusBarColor(this,R.color.white,true)

        mBinding.recyclerImage.layoutManager = mLayoutManager

        mBinding.recyclerImage.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                DensityUtil.dip2px(this, 8f),
                false
            )
        )
        mBinding.recyclerImage.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        //恢复数据
        if (savedInstanceState?.getParcelableArrayList<LocalMedia>(BUNDLE_IMAGE_KEY) != null) {
            val localData =
                savedInstanceState.getParcelableArrayList<LocalMedia>(BUNDLE_IMAGE_KEY)!!

            if (BuildConfig.DEBUG) {
                if (localData.isNullOrEmpty().not()) {
                    localData.forEach {
                        LogUtils.e(
                            "savedInstanceState-->${it.path}"
                        )
                    }
                }
            }

            mPicList.clear()
            mPicList.addAll(savedInstanceState.getParcelableArrayList(BUNDLE_IMAGE_KEY)!!)
        }

        mAdapter.setDatas(mPicList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mAdapter.getData().isNullOrEmpty().not()) {
            outState.putParcelableArrayList(BUNDLE_IMAGE_KEY, mAdapter.getArrayData())
        }
    }

    override fun createObserver() {
        mViewModel.internetPublishStateLiveData.observe(this){
            if(!it.isNullOrEmpty()){
                ToastUtils.showShort("发布成功")
                TempManager.getInstance().setTempImageList(it)
                val intent = Intent()
                intent.putExtra("test","list")
                setResult(RESULT_OK,intent)
                finish()
                //EventBus.getDefault().post(xxxxxx) or postStxxx()
            }else{
                //xxxxxxxx
                ToastUtils.showShort("发布异常")
            }
        }
        mViewModel.dialogLiveData.observe(this){
            if(it){
                WaitDialog.show(this, "发布中...")
            }else{
                WaitDialog.dismiss()
            }
        }
    }

    override fun initListener() {
        mBinding.leftBack.setOnClickListener {
            finish()
        }

        mBinding.tvPublish.setOnClickListener {
            checkAndPublish()
        }

        mAdapter.setOnItemClickListener(object : ImageGridAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {}

            override fun openPicture() {
                PicSelectUtils.publishImageSingle(this@PublishNewsActivity) {
                    runOnUiThread {
                        //  测试   val isMaxSize = result.size == mAdapter.getSelectMax()
                        //   val oldSize: Int = mAdapter.getData().size
                        /*      mAdapter.notifyItemRangeRemoved(0, if (isMaxSize) oldSize + 1 else oldSize)
                              mAdapter.getData().clear()*/

                        val data = mAdapter.getData()
                        val tempData = mutableListOf<LocalMedia>()
                        tempData.addAll(data)
                        if (tempData.contains(it)) return@runOnUiThread
                        tempData.add(it)
                        /* 测试
                         if(mAdapter.getData().size<mAdapter.getSelectMax()){
                              mAdapter.notifyItemRangeInserted(0, result.size)
                          }else{
                              mAdapter.notifyItemChanged(0)
                          }*/
                        mAdapter.updateListItems(tempData)
                    }
                }

            }
        })
    }

    /**
     * 检查数据并发布
     */
    private fun checkAndPublish() {
        if (mAdapter.getData().isNullOrEmpty()) {
            ToastUtils.showShort("至少选择一张图片发布")
            return
        }
        mViewModel.uploadSyncImage(mAdapter.getData())
    }

    companion object {

        const val INTENT_CODE = 101
        const val BUNDLE_IMAGE_KEY = "bundle_image"

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PublishNewsActivity::class.java)
            context.startActivity(starter)
        }

        @JvmStatic
        fun startFotResult(context: Context) {
            val starter = Intent(context, PublishNewsActivity::class.java)
            (context as Activity).startActivityForResult(starter,INTENT_CODE)
        }
    }
}
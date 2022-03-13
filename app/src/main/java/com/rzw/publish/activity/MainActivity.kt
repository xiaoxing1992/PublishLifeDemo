package com.rzw.publish.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.recyclerview.widget.GridLayoutManager
import com.luck.picture.lib.utils.DensityUtil
import com.rzw.publish.R
import com.rzw.publish.adapter.ImageAdapter
import com.rzw.publish.adapter.ImageGridAdapter
import com.rzw.publish.base.BaseActivity
import com.rzw.publish.databinding.ActivityMainBinding
import com.rzw.publish.manager.TempManager
import com.rzw.publish.utils.MBBarUtil
import com.rzw.publish.view.FullyGridLayoutManager
import com.rzw.publish.view.GridSpacingItemDecoration
import com.rzw.publish.viewmodel.CommonViewModel
import android.widget.Toast
import kotlin.system.exitProcess

/**
 * 主界面 展示数据
 */
class MainActivity : BaseActivity<CommonViewModel,ActivityMainBinding>() {

    private var exitTime: Long = 0

    private val mLayoutManager: FullyGridLayoutManager by lazy {
        FullyGridLayoutManager(
            this,
            3,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    private val mAdapter: ImageAdapter by lazy { ImageAdapter(this) }

    override fun layoutId(): Int  = R.layout.activity_main

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
        mBinding.tvGotoPublic.setOnClickListener {
            PublishNewsActivity.startFotResult(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK&&requestCode==PublishNewsActivity.INTENT_CODE){
            val list = TempManager.getInstance().getTempImageList()
            val adapterList = mutableListOf<String>()
            adapterList.addAll(list)
            mAdapter.setList(adapterList)
            TempManager.getInstance().resetTempImageList()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(applicationContext, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
                exitProcess(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
package com.rzw.publish.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseActivity<VM:BaseViewModel,DB:ViewDataBinding>:AppCompatActivity() {

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        initView(savedInstanceState)
        initData(savedInstanceState)
        createObserver()
        initListener()
    }

    open fun layoutId(): Int = 0
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData(savedInstanceState: Bundle?)
    open fun createObserver(){}
    open fun initListener(){}

    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            when {
                ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java -> {
                    if (layoutId() == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
                    mBinding = DataBindingUtil.setContentView(this, layoutId())
                    (mBinding as ViewDataBinding).lifecycleOwner = this
                }
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, layoutInflater) as DB
                        setContentView(mBinding.root)
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    setContentView(layoutId())
                }
            }
            createViewModel(type.actualTypeArguments[0])
        } else throw IllegalArgumentException("Generic error")
    }

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(type: Type) {
        val tClass = type as? Class<VM> ?: BaseViewModel::class.java
        mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
            .get(tClass) as VM
    }
}
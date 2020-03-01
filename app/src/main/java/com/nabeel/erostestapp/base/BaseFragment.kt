package com.akbartravel.erostest.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected var mViewModel: VM? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        initVM()
     //   setListeners()
        startObserve()
        setData()
        getData()
        return provideYourFragmentView(inflater, container, savedInstanceState)
    }

    abstract fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    open fun prepareBeforeInitView() {}
    open fun initView() {}
    open fun startObserve() {}
  //  open fun setListeners() {}
    open fun setData() {}
    open fun getData(){}

    private fun initVM() {
        providerVMClass()?.let { it ->
            mViewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(mViewModel!!)
        }
    }

    /**
     * [BaseViewModel]
     */
    open fun providerVMClass(): Class<VM>? = null

    override fun onDestroy() {
        mViewModel?.let {
            lifecycle.removeObserver(it)
        }

        super.onDestroy()
    }
}
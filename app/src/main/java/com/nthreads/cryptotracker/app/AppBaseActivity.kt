package com.nthreads.cryptotracker.app

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class AppBaseActivity<VM : ViewModel, DB : ViewDataBinding>(
    viewModelClass: Class<VM>,
    @LayoutRes
    private val layoutResId: Int
) : AppCompatActivity() {

    lateinit var binding: DB

    private val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    open fun init() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId) as DB
        binding.run {
            //setVariable(BR.viewModel, viewModel)
            lifecycleOwner = this@AppBaseActivity
        }

        init()
    }

}
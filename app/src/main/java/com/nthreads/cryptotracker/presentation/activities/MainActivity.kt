package com.nthreads.cryptotracker.presentation.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.nthreads.cryptotracker.R
import com.nthreads.cryptotracker.databinding.ActivityMainBinding
import com.nthreads.cryptotracker.domain.binders.MainViewModel
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import com.nthreads.cryptotracker.domain.models.Resource.Status.*
import com.nthreads.cryptotracker.presentation.viewmodels.CryptoExchangeViewModel
import java.text.DecimalFormat


/*class MainActivity : AppBaseActivity<CryptoExchangeViewModel, ActivityMainBinding>(
    viewModelClass = CryptoExchangeViewModel::class.java,
    layoutResId = R.layout.activity_main
) {*/

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CryptoExchangeViewModel::class.java) }
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        setObserver()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getCurrentPrice()
        }

        binding.layoutMinRateLimit.setOnClickListener {
            activityForResult.launch(Intent(this, AlertActivity::class.java))
        }

        binding.layoutMaxRateLimit.setOnClickListener {
            activityForResult.launch(Intent(this, AlertActivity::class.java))
        }
    }

    private val activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

        }
    }

    private fun setObserver() {
        viewModel.cryptoRateResource.observe(this) { resource ->
            resource?.let {
                binding.swipeToRefresh.isRefreshing = false

                when (it.status) {
                    LOADING -> {}
                    SUCCESS -> {
                        val usd = it.data?.bpi?.usd?: CurrencyRate()
                        binding.viewmodel = MainViewModel(usd, 119f, 121f)
                        Log.d("TAG", "onCreate: ${it.data?.bpi?.usd}")
                    }
                    ERROR -> {}
                    EMPTY -> {}
                }

            }
        }

        viewModel.getCurrentPrice()
    }
}
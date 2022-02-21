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
import com.nthreads.cryptotracker.app.Consts
import com.nthreads.cryptotracker.databinding.ActivityMainBinding
import com.nthreads.cryptotracker.domain.binders.MainViewModel
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import com.nthreads.cryptotracker.domain.models.Resource.Status.*
import com.nthreads.cryptotracker.domain.services.MyNotificationManager
import com.nthreads.cryptotracker.presentation.viewmodels.CryptoExchangeViewModel
import com.nthreads.cryptotracker.utils.PreferenceUtility


/*class MainActivity : AppBaseActivity<CryptoExchangeViewModel, ActivityMainBinding>(
    viewModelClass = CryptoExchangeViewModel::class.java,
    layoutResId = R.layout.activity_main
) {*/

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CryptoExchangeViewModel::class.java) }
    private lateinit var binding: ActivityMainBinding

    private val mainViewMode: MainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this


        getSavedAlerts()
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


        binding.viewmodel = mainViewMode
    }

    private val activityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val min = it.getFloatExtra(Consts.KEY_MIN_LIMIT, 10000f)
                    val max = it.getFloatExtra(Consts.KEY_MAX_LIMIT, 40000f)

                    updateObserverModel(min, max)
                    PreferenceUtility.setPreference(
                        this,
                        Consts.APP_PREFS,
                        Consts.KEY_MIN_LIMIT,
                        min
                    )
                    PreferenceUtility.setPreference(
                        this,
                        Consts.APP_PREFS,
                        Consts.KEY_MAX_LIMIT,
                        max
                    )
                }

            }
        }

    private fun getSavedAlerts() {
        val min = PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, Consts.KEY_MIN_LIMIT)
        val max = PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, Consts.KEY_MAX_LIMIT)

        updateObserverModel(min, max)
    }

    private fun updateObserverModel(min: Float, max: Float) {
        mainViewMode.minLimit = min
        mainViewMode.maxLimit = max

        binding.viewmodel = mainViewMode

        notifyRateIfNeeded(mainViewMode.currency.rateFloat)
    }

    private fun notifyRateIfNeeded(currRate: Float) {
        val message: String = if (currRate <= mainViewMode.minLimit) {
            getString(R.string.msg_min_rate_threshold, mainViewMode.minLimit, currRate)
        } else if (currRate >= mainViewMode.maxLimit) {
            getString(R.string.msg_max_rate_threshold, mainViewMode.maxLimit, currRate)
        } else {
            return
        }

        MyNotificationManager.sendNotification(message, this)
    }

    private fun setObserver() {
        viewModel.cryptoRateResource.observe(this) { resource ->
            resource?.let {
                binding.swipeToRefresh.isRefreshing = false

                when (it.status) {
                    LOADING -> {}
                    SUCCESS -> {
                        val usd = it.data?.bpi?.usd ?: CurrencyRate()
                        mainViewMode.currency = usd
                        binding.viewmodel = mainViewMode
                        notifyRateIfNeeded(usd.rateFloat)
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
package com.nthreads.cryptotracker.presentation.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nthreads.cryptotracker.R
import com.nthreads.cryptotracker.app.Consts
import com.nthreads.cryptotracker.databinding.ActivityMainBinding
import com.nthreads.cryptotracker.domain.binders.MainViewModel
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import com.nthreads.cryptotracker.domain.models.Resource.Status.*
import com.nthreads.cryptotracker.domain.workers.PriceAlertWorker
import com.nthreads.cryptotracker.presentation.viewmodels.CryptoExchangeViewModel
import com.nthreads.cryptotracker.utils.PreferenceUtility

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CryptoExchangeViewModel::class.java) }
    private lateinit var binding: ActivityMainBinding

    private lateinit var lbManager: LocalBroadcastManager
    private val mainViewMode: MainViewModel = MainViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        lbManager = LocalBroadcastManager.getInstance(this)

        getDataFromCache()
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

        PriceAlertWorker.cancelOldWorkersIfAny(applicationContext)
        PriceAlertWorker.startOneTimeWorkRequestAndScheduleAnother(applicationContext)
    }

    private val priceAlertBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            binding.isLoading = true
            val currPrice = intent.getParcelableExtra<CurrencyRate>(Consts.KEY_CURRENT_PRICE) as CurrencyRate
            mainViewMode.currency = currPrice
            binding.viewmodel = mainViewMode
            binding.isLoading = false
            binding.isError = false
            Log.d("receiver", "Got currPrice: ${currPrice.rateFloat}")
        }
    }

    override fun onResume() {
        super.onResume()
        lbManager.registerReceiver(
            priceAlertBroadcastReceiver,
            IntentFilter(Consts.PRICE_ALERT_EVENT)
        )
    }

    override fun onPause() {
        super.onPause()
        lbManager.unregisterReceiver(priceAlertBroadcastReceiver)
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


    private fun getDataFromCache() {
        val min = PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, Consts.KEY_MIN_LIMIT)
        val max = PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, Consts.KEY_MAX_LIMIT)

        val rate = PreferenceUtility.getFloatPreference(this, Consts.APP_PREFS, Consts.PREF_LAST_PRICE)
        val code = PreferenceUtility.getPreference(this, Consts.APP_PREFS, Consts.PREF_PRICE_CODE)
        val symbol = PreferenceUtility.getPreference(this, Consts.APP_PREFS, Consts.PREF_PRICE_SYMBOL)
        val desc = PreferenceUtility.getPreference(this, Consts.APP_PREFS, Consts.PREF_PRICE_DESC)

        mainViewMode.currency = CurrencyRate(code = code, symbol = symbol, rateFloat = rate, description = desc)

        updateObserverModel(min, max)
    }

    private fun updateObserverModel(min: Float, max: Float) {
        mainViewMode.minLimit = min
        mainViewMode.maxLimit = max

        binding.viewmodel = mainViewMode
    }

    private fun setObserver() {
        viewModel.cryptoRateResource.observe(this) { resource ->
            resource?.let {
                binding.swipeToRefresh.isRefreshing = false

                when (it.status) {
                    LOADING -> {
                        binding.isLoading = true
                        binding.isError = false
                    }
                    SUCCESS -> {
                        binding.isLoading = false
                        binding.isError = false

                        val usd = it.data?.bpi?.usd ?: CurrencyRate()
                        mainViewMode.currency = usd
                        binding.viewmodel = mainViewMode

                        Log.d("TAG", "Success: ${it.data?.bpi?.usd}")
                    }
                    ERROR -> {
                        binding.isLoading = false
                        binding.isError = true
                    }
                    EMPTY -> {
                        binding.isLoading = false
                        binding.isError = true
                    }
                }

            }
        }

        viewModel.getCurrentPrice()
    }
}
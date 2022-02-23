package com.nthreads.cryptotracker.domain.workers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.*
import com.nthreads.cryptotracker.app.Consts
import com.nthreads.cryptotracker.data.remote.repos.CryptoExchangeRepository
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import com.nthreads.cryptotracker.domain.usecases.NotifyAlertUseCase
import com.nthreads.cryptotracker.utils.PreferenceUtility
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class PriceAlertWorker(private val appContext : Context, workerParams : WorkerParameters) : Worker(appContext, workerParams){

    private val disposable = CompositeDisposable()

    override fun doWork(): Result {
        return try {
            getCurrentPrice()
            startOneTimeWorkRequestAndScheduleAnother(appContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @Throws(Throwable::class)
    private fun getCurrentPrice()  {
        val repository = CryptoExchangeRepository()
        disposable.add(repository.getCurrentPrice().subscribe ({
            val rate = it.bpi.usd

            val intent = Intent(Consts.PRICE_ALERT_EVENT)
            intent.putExtra(Consts.KEY_CURRENT_PRICE, rate)
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent)

            NotifyAlertUseCase.notifyIfNeeded(appContext, rate.rateFloat)

            savePrefs(rate)
        }, {
            throw it
        }))
    }

    private fun savePrefs(rate: CurrencyRate) {
        PreferenceUtility.setPreference(appContext, Consts.APP_PREFS, Consts.PREF_LAST_PRICE, rate.rateFloat)
        PreferenceUtility.setPreference(appContext, Consts.APP_PREFS, Consts.PREF_PRICE_SYMBOL, rate.symbol)
        PreferenceUtility.setPreference(appContext, Consts.APP_PREFS, Consts.PREF_PRICE_CODE, rate.code)
        PreferenceUtility.setPreference(appContext, Consts.APP_PREFS, Consts.PREF_PRICE_DESC, rate.description)
    }

    companion object {
        const val TAG_WORKER_SINGLE_RECURRING = "OneTimeWorker"
        const val TAG_WORKER_PERIODIC = "PeriodicWorker"

        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        fun startPeriodicWorkRequest(appContext: Context) {
            Log.d("Worker", "Scheduled Periodic Job after every 15 minutes to check connectivity")
            val recurringWork: PeriodicWorkRequest =
                PeriodicWorkRequest.Builder(PriceAlertWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                    .setConstraints(constraints)
                    .addTag(TAG_WORKER_PERIODIC).build()

            WorkManager.getInstance(appContext).enqueue(recurringWork)
        }

        fun startOneTimeWorkRequestAndScheduleAnother(appContext: Context) {
            Log.d("Worker", "startOneTimeWorkRequestAndScheduleAnother")

            val work = OneTimeWorkRequest.Builder(PriceAlertWorker::class.java)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(TAG_WORKER_SINGLE_RECURRING)
                .build()
            WorkManager.getInstance(appContext).enqueue(work)
        }

        fun cancelOldWorkersIfAny(appContext: Context, tag : String = TAG_WORKER_SINGLE_RECURRING) {
            WorkManager.getInstance(appContext).cancelAllWorkByTag(tag)
        }
    }

    override fun onStopped() {
        super.onStopped()
        disposable.clear()
    }
}
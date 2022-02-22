package com.nthreads.cryptotracker.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.nthreads.cryptotracker.BuildConfig

class CryptoApplication : Application(), Configuration.Provider{
    override fun getWorkManagerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()

        /*val configs = if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }

        WorkManager.initialize(this, configs)*/
    }
}
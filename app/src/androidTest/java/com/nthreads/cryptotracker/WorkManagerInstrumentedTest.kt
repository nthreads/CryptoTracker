package com.nthreads.cryptotracker

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.nthreads.cryptotracker.domain.workers.PriceAlertWorker
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkManagerInstrumentedTest {
    lateinit var appContext : Context
    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)
    }

    @Test
    fun testPriceAlertWorker() {

        // Create request
        val request = OneTimeWorkRequestBuilder<PriceAlertWorker>().build()

        val workManager = WorkManager.getInstance(appContext)
        workManager.enqueue(request).result.get()
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
    }
}
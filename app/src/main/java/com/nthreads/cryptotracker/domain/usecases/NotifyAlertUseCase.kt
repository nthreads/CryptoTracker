package com.nthreads.cryptotracker.domain.usecases

import android.content.Context
import android.util.Log
import com.nthreads.cryptotracker.R
import com.nthreads.cryptotracker.app.Consts
import com.nthreads.cryptotracker.domain.services.MyNotificationManager
import com.nthreads.cryptotracker.utils.PreferenceUtility

object NotifyAlertUseCase {
    fun notifyIfNeeded(context: Context, currRate: Float) {

        val min = PreferenceUtility.getFloatPreference(context, Consts.APP_PREFS, Consts.KEY_MIN_LIMIT)
        val max = PreferenceUtility.getFloatPreference(context, Consts.APP_PREFS, Consts.KEY_MAX_LIMIT)

        Log.d("TAG", "notifyIfNeeded: Curr = $currRate , Min. = $min , Max. = $max")
        val message: String = when {
            currRate <= min -> {
                context.getString(R.string.msg_min_rate_threshold, min, currRate)
            }
            currRate >= max -> {
                context.getString(R.string.msg_max_rate_threshold, max, currRate)
            }
            else -> {
                return
            }
        }

        MyNotificationManager.sendNotification(message, context)
    }

}
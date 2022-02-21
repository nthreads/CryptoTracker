package com.nthreads.cryptotracker.utils

import java.text.DecimalFormat

fun getRateFormatted(rate : Float): String {
    val dform = DecimalFormat("#,###.##")
    return dform.format(rate)
}
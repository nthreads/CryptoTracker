package com.nthreads.cryptotracker.domain.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.nthreads.cryptotracker.domain.models.BpiTime
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import kotlinx.parcelize.Parcelize

@Parcelize
class BPIResponse(
    var time: BpiTime,
    var disclaimer: String,
    var bpi: BPI
) : Parcelable

// Bitcoin Price Index (BPI)
@Parcelize
class BPI(
    @SerializedName("USD")
    val usd: CurrencyRate,
    @SerializedName("GBP")
    val gbp: CurrencyRate,
    @SerializedName("EUR")
    val eur: CurrencyRate
) : Parcelable
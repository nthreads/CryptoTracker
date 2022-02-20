package com.nthreads.cryptotracker.domain.responses

import com.google.gson.annotations.SerializedName
import com.nthreads.cryptotracker.domain.models.BpiTime
import com.nthreads.cryptotracker.domain.models.CurrencyRate


class BPIResponse(
    var time: BpiTime,
    var disclaimer: String,
    var bpi: BPI
)

// Bitcoin Price Index (BPI)
class BPI(
    @SerializedName("USD")
    val usd: CurrencyRate,
    @SerializedName("GBP")
    val gbp: CurrencyRate,
    @SerializedName("EUR")
    val eur: CurrencyRate
)
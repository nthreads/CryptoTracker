package com.nthreads.cryptotracker.domain.models

import com.google.gson.annotations.SerializedName

class Currency(
    @SerializedName("currency")
    val code: String,
    val country: String
)
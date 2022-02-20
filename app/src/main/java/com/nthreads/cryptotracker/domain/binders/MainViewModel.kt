package com.nthreads.cryptotracker.domain.binders

import com.nthreads.cryptotracker.domain.models.CurrencyRate

class MainViewModel (val currency: CurrencyRate, val minLimit : Float = 0f, val maxLimit : Float = 0f)
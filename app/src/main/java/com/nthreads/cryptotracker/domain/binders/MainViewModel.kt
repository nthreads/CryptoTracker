package com.nthreads.cryptotracker.domain.binders

import com.nthreads.cryptotracker.domain.models.CurrencyRate
import java.text.DecimalFormat

class MainViewModel (var currency: CurrencyRate = CurrencyRate(), var minLimit : Float = 0f, var maxLimit : Float = 0f)
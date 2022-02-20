package com.nthreads.cryptotracker.data.remote.services

import com.nthreads.cryptotracker.domain.models.Currency
import com.nthreads.cryptotracker.domain.models.CurrencyRate
import com.nthreads.cryptotracker.domain.responses.BPI
import com.nthreads.cryptotracker.domain.responses.BPIResponse
import com.nthreads.cryptotracker.domain.responses.BasicResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoExchangeService {

    @GET("currentprice.json")
    fun getCurrentPrice(): Single<BPIResponse>

    @GET("{currency}.json")
    fun getCurrencyRate(@Path("currency") currencyCode : String = "USD"): Single<CurrencyRate>


    @GET("supported-currencies.json")
    fun getSupportedCurrencies(): Single<List<Currency>>
}
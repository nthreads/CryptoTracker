package com.nthreads.cryptotracker.data.remote.repos

import com.nthreads.cryptotracker.data.remote.ApiManager
import com.nthreads.cryptotracker.data.remote.services.CryptoExchangeService
import com.nthreads.cryptotracker.domain.responses.BPIResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CryptoExchangeRepository {

    fun getCurrentPrice(): Single<BPIResponse> {
        return ApiManager.newRequest(CryptoExchangeService::class.java)
            .getCurrentPrice()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
package com.nthreads.cryptotracker.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nthreads.cryptotracker.data.remote.repos.CryptoExchangeRepository
import com.nthreads.cryptotracker.domain.models.Resource
import com.nthreads.cryptotracker.domain.responses.BPI
import com.nthreads.cryptotracker.domain.responses.BPIResponse
import com.nthreads.cryptotracker.utils.getMessageResValue
import io.reactivex.disposables.CompositeDisposable

class CryptoExchangeViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    private val repository = CryptoExchangeRepository()

    val cryptoRateResource: MutableLiveData<Resource<BPIResponse>> = MutableLiveData()

    fun getCurrentPrice() {
        cryptoRateResource.value = Resource.loading()

        disposable.add(repository.getCurrentPrice()
            .subscribe({
                cryptoRateResource.value = Resource.success(it)
            }, {
                it.printStackTrace()
                cryptoRateResource.value = Resource.error(it.getMessageResValue())
            })
        )

    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
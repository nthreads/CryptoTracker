package com.nthreads.cryptotracker.domain.binders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AlertViewModel {
    private val _minLimit = MutableLiveData<Float>()
    private val _maxLimit = MutableLiveData<Float>()


    val minLimit : LiveData<Float> = _minLimit
    val maxLimit : LiveData<Float> = _maxLimit


}
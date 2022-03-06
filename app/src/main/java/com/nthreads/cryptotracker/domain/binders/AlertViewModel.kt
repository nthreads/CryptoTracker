package com.nthreads.cryptotracker.domain.binders

import androidx.lifecycle.MutableLiveData

class AlertViewModel {


    val minLimit = MutableLiveData<Float>()
    val maxLimit = MutableLiveData<Float>()

    fun onValueChanged(values: List<Float>) {
        if (values.isEmpty()) {
            minLimit.value = values[0]
            maxLimit.value = values[1]
        }
    }
}
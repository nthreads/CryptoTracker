package com.nthreads.cryptotracker.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.RangeSlider

object DataBinderAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onValueChangeListener"])
    fun setOnValueChangeListener(slider: RangeSlider, listener: OnValueChangeListener) {
        slider.addOnChangeListener { sl: RangeSlider, _: Float, _: Boolean ->
            listener.onValueChanged(sl.values)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["textViewLabel1", "textViewLabel2"], requireAll = false)
    fun setTextViewLabel(slider: RangeSlider, textViewLabel1: TextView, textViewLabel2: TextView) {
        slider.addOnChangeListener { sl: RangeSlider, _: Float, _: Boolean ->
            "$${getRateFormatted(sl.values[0])}".also { textViewLabel1.text = it }
            "$${getRateFormatted(sl.values[1])}".also { textViewLabel2.text = it }
        }
    }


    interface OnValueChangeListener {
        fun onValueChanged(value: List<Float>)
    }
}
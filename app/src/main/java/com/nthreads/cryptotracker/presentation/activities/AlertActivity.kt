package com.nthreads.cryptotracker.presentation.activities

import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.slider.RangeSlider
import com.nthreads.cryptotracker.R
import com.nthreads.cryptotracker.app.Consts
import com.nthreads.cryptotracker.databinding.ActivityAlertBinding
import com.nthreads.cryptotracker.domain.binders.AlertViewModel
import com.nthreads.cryptotracker.domain.binders.MainViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AlertActivity : AppCompatActivity() {

    val alertViewModel = AlertViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAlertBinding = DataBindingUtil.setContentView(this, R.layout.activity_alert)
        binding.viewmodel = alertViewModel

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.rangeSlider.setLabelFormatter{ value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        binding.rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            alertViewModel.minLimit.value = slider.values[0]
            alertViewModel.maxLimit.value = slider.values[1]
        })

        binding.btnCreateAlert.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Consts.KEY_MIN_LIMIT, binding.rangeSlider.values[0])
            intent.putExtra(Consts.KEY_MAX_LIMIT, binding.rangeSlider.values[1])
            finish()
            setResult(RESULT_OK, intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
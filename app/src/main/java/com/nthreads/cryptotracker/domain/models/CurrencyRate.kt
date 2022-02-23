package com.nthreads.cryptotracker.domain.models

import android.os.Parcelable
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CurrencyRate(
    val code: String = "",
    val symbol: String = "",
    val rate: String = "",
    val description: String = "",
    @SerializedName("rate_float")
    val rateFloat: Float = 0f
) : Parcelable {

    fun getSymbolHtml(): Spanned {
        return HtmlCompat.fromHtml(symbol, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
package com.nthreads.cryptotracker.domain.models

import android.os.Build
import android.os.Parcelable
import android.text.Html
import android.text.Spanned
import androidx.versionedparcelable.ParcelField
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat
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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(symbol, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(symbol)
        }
    }
}
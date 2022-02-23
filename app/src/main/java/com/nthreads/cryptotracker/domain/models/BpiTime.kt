package com.nthreads.cryptotracker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BpiTime(
    val updated: String,
    val updatedISO: String
) : Parcelable
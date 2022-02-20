package com.nthreads.cryptotracker.utils

import com.nthreads.cryptotracker.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException


fun Throwable.getMessageResValue(): Int {

    var errorMsg: Int = R.string.toast_err_went_wrong

    when (this) {
        is HttpException -> {
            errorMsg = when (code()) {
                400 -> R.string.toast_err_unauthorized
                401 -> R.string.toast_err_session
                403 -> R.string.toast_err_forbidden_req
                500 -> R.string.toast_err_internal_server
                else -> R.string.toast_err_connection
            }
        }
        is ConnectException -> {
            errorMsg = R.string.toast_err_network
        }
        is SocketTimeoutException -> {
            errorMsg = R.string.toast_err_time_out
        }
    }

    return errorMsg
}

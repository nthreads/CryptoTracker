package com.nthreads.cryptotracker.domain.models

import com.nthreads.cryptotracker.domain.models.Resource.Status.*

//This class describes data with a status
class Resource<T> constructor(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val messageRes: Int? = null
) {
    enum class Status {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data = data)
        }

        fun <T> empty(): Resource<T> {
            return Resource(status = EMPTY)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(status = ERROR, message = msg)
        }

        fun <T> error(msg: Int?): Resource<T> {
            return Resource(status = ERROR, messageRes = msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(status = LOADING)
        }
    }
}
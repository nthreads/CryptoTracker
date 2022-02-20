package com.nthreads.cryptotracker.domain.responses

import com.nthreads.cryptotracker.domain.models.BpiTime


class BasicResponse<T>(
    var data: T
)
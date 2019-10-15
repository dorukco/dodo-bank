package com.dodo.bank.exception

import io.micronaut.http.HttpStatus

class ApiException : RuntimeException {

    private val status: HttpStatus
    private val apiError: ApiError

    constructor(status: HttpStatus, apiError: ApiError) : super(apiError.message) {
        this.status = status
        this.apiError = apiError
    }

    fun status() = status
    fun apiError() = apiError
}

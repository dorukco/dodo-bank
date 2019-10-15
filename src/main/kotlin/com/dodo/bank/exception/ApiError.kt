package com.dodo.bank.exception

import io.micronaut.http.HttpStatus

data class ApiError(val message: String) {

    companion object {
        fun ofMessage(message: String) = ApiError(message)
    }

    fun asBadRequest() = ApiException(HttpStatus.BAD_REQUEST, this)

    fun asNotFound() = ApiException(HttpStatus.NOT_FOUND, this)
}

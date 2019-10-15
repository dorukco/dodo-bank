package com.dodo.bank.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
@Requires(classes = [ApiException::class, ExceptionHandler::class])
open class CommonExceptionHandler : ExceptionHandler<Exception, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, e: Exception): HttpResponse<ApiError> {
        return when (e) {
            is ApiException -> {
                when (e.status()) {
                    HttpStatus.NOT_FOUND -> HttpResponse.notFound(ApiError.ofMessage(e.apiError().message))
                    HttpStatus.BAD_REQUEST -> HttpResponse.badRequest(ApiError.ofMessage(e.apiError().message))
                    else -> HttpResponse.serverError(ApiError.ofMessage("Unexpected API error"))
                }
            }
            else -> HttpResponse.serverError(ApiError.ofMessage("Unexpected server error"))
        }

    }

}
